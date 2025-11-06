package com.krshashi.gpscam.app.ui.camera

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.PixelCopy
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import androidx.core.view.doOnLayout
import androidx.exifinterface.media.ExifInterface
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.LatLng
import com.krshashi.gpscam.app.ui.camera.LocationViewModel
import com.krshashi.gpscam.app.R
import com.krshashi.gpscam.app.domain.AddressDetails
import com.krshashi.gpscam.app.ui.components.AddressInfoCard
import com.krshashi.gpscam.app.ui.components.CaptureButton
import com.krshashi.gpscam.app.ui.components.ErrorCard
import com.krshashi.gpscam.app.ui.components.MapView
import com.krshashi.gpscam.app.ui.components.ProgressIndicator
import com.krshashi.gpscam.app.ui.theme.AppIcons
import com.krshashi.gpscam.app.utils.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun CameraScreen(
    onNavigateToGallery: () -> Unit,
    modifier: Modifier = Modifier,
    locationViewModel: LocationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    var flashMode by remember { mutableIntStateOf(ImageCapture.FLASH_MODE_OFF) }

    val preview = remember { Preview.Builder().build() }
    val previewView = remember { PreviewView(context) }
    var capturingState by remember { mutableStateOf(false) }

    // 🔸 ImageCapture with default flash mode
    val imageCapture = remember {
        ImageCapture.Builder()
            .setFlashMode(ImageCapture.FLASH_MODE_OFF)
            .build()
    }

    // 👇 Bind camera when lensFacing changes
    LaunchedEffect(lensFacing) {
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val cameraProvider = cameraProviderFuture.get()
        cameraProvider.unbindAll()

        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

        preview.surfaceProvider = previewView.surfaceProvider
    }

    CameraContents(
        previewView = previewView,
        viewModel = locationViewModel,
        captureState = capturingState,
        onGalleryClick = onNavigateToGallery,
        onFlashClick = {
            flashMode = when (flashMode) {
                ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_ON
                ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_AUTO
                else -> ImageCapture.FLASH_MODE_OFF
            }
            imageCapture.flashMode = flashMode
        },
        onSwitchCamera = {
            lensFacing = when(lensFacing){
                CameraSelector.LENS_FACING_BACK -> CameraSelector.LENS_FACING_FRONT
                else -> CameraSelector.LENS_FACING_BACK
            }
        },
        onCapture = { overlayView ->
            capturingState = true
            // Ensure overlay is laid out before combining
            overlayView.doOnLayout {
                imageCapture.takePicture(
                    context = context,
                    onSuccess = { output ->
                        capturingState = false

                        output.savedUri?.let { rawUri ->
                            coroutineScope.launch {
                                combineImageAndOverlay(
                                    rawImageUri = rawUri,
                                    overlayView = overlayView,
                                    context = context,
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Saved Successfully!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    },
                                    onError = { msg ->
                                        Toast.makeText(
                                            context,
                                            msg,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                )
                            }
                        }
                    },
                    onError = { error ->
                        Toast.makeText(context, "Capture error: $error", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        },
        modifier = modifier,
        flashMode = flashMode
    )
}

@Composable
private fun CameraContents(
    previewView: PreviewView,
    captureState: Boolean,
    viewModel: LocationViewModel,
    flashMode: Int,
    modifier: Modifier = Modifier,
    onFlashClick: () -> Unit,
    onSwitchCamera: () -> Unit,
    onGalleryClick: () -> Unit,
    onCapture: (ComposeView) -> Unit
) {
    val context = LocalContext.current
    val overlayComposeView = remember { ComposeView(context) }

    Column(modifier) {
        // Top Row: Flash button
        Row(
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onFlashClick) {
                Icon(
                    painter = painterResource(
                        when (flashMode) {
                            ImageCapture.FLASH_MODE_AUTO -> AppIcons.FlashAuto
                            ImageCapture.FLASH_MODE_ON -> AppIcons.FlashOn
                            else -> AppIcons.FlashOff
                        }
                    ),
                    contentDescription = "camera_flash_icon",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            }
        }

        // Camera Preview with overlay
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )

            AndroidView(
                factory = {
                    overlayComposeView.apply {
                        setContent {
                            val addressState by viewModel.appResultFlow.collectAsStateWithLifecycle(
                                initialValue = ResultState.Loading(true)
                            )

                            LaunchedEffect(addressState) {
                                Log.d("LocationState", "AddressUiState: $addressState")
                            }

                            OverlayContent(addressState)
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Capture button
        CaptureButton(
            onClick = { onCapture(overlayComposeView) },
            contentColor = MaterialTheme.colorScheme.secondary,
            indicateProgress = captureState,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .size(64.dp)
                .align(Alignment.CenterHorizontally)
        )

        // Bottom row: Photos and camera switch
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            IconButton(onClick = onGalleryClick) {
                Icon(
                    painter = painterResource(AppIcons.Gallery),
                    contentDescription = "gallery_icon",
                    modifier = Modifier.size(24.dp)
                )
            }
            OutlinedButton(onClick = { /* TODO: Ignore for now! */ }) {
                Text(text = "Photos")
            }
            IconButton(onClick = onSwitchCamera) {
                Icon(
                    painter = painterResource(AppIcons.CameraSwitch),
                    contentDescription = "camera_switch_icon",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

/** Suspend function to safely get CameraProvider */
suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCancellableCoroutine { cont ->
        val future = ProcessCameraProvider.getInstance(this)
        future.addListener({
            cont.resume(future.get()) { cause, _, _ -> }
        }, ContextCompat.getMainExecutor(this))
    }


fun ImageCapture.takePicture(
    context: Context,
    onSuccess: (ImageCapture.OutputFileResults) -> Unit,
    onError: (String) -> Unit
) {
    val outputOptions = ImageCapture.OutputFileOptions.Builder(
        File(context.cacheDir, "temp_image.jpg")
    ).build()

    this.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onSuccess(outputFileResults)
            }

            override fun onError(exception: ImageCaptureException) {
                onError(exception.message.toString())
            }
        })
}

@Composable
fun OverlayContent(addressState: ResultState<AddressDetails>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedContent(
            targetState = addressState,
            transitionSpec = {
                fadeIn() + expandVertically() togetherWith fadeOut() + shrinkVertically()
            },
            label = "address_state_animation"
        ) { state ->

            when (state) {
                is ResultState.Failure -> ErrorCard(
                    message = state.message
                )

                is ResultState.Loading -> ProgressIndicator(
                    label = stringResource(R.string.fetching_details_lbl)
                )

                is ResultState.Success<AddressDetails> -> Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(96.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        MapView(
                            latlng = LatLng(
                                state.data.latitude,
                                state.data.longitude
                            ),
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    AddressInfoCard(
                        addressLine1 = state.data.addressLine1,
                        addressLine2 = state.data.addressLine2,
                        coordinates = stringResource(
                            R.string.coordinate_value,
                            state.data.latitude,
                            state.data.longitude
                        ),
                        datetime = state.data.timestamp,
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 96.dp)
                    )
                }
            }
        }
    }
}

private suspend fun combineImageAndOverlay(
    context: Context,
    rawImageUri: Uri,
    overlayView: ComposeView,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    // Ensure overlay has been laid out
    if (overlayView.width == 0 || overlayView.height == 0) {
        onError("Overlay not ready for capture.")
        return
    }

    // Launch on background thread
    withContext(Dispatchers.IO) {
        try {
            // 1. Decode the camera image (no changes here)
            val cameraBitmap = decodeCameraBitmap(context, rawImageUri)

            // 2. Get overlay bitmap using PixelCopy
            val overlayBitmap = captureViewToBitmap(overlayView)

            // 3. Scale overlay to match camera bitmap dimensions (no changes here)
            val scaledOverlay = overlayBitmap.scale(cameraBitmap.width, cameraBitmap.height)

            // 4. Combine both bitmaps (no changes here)
            val finalBitmap = createBitmap(
                cameraBitmap.width,
                cameraBitmap.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(finalBitmap)
            canvas.drawBitmap(cameraBitmap, 0f, 0f, null)
            canvas.drawBitmap(scaledOverlay, 0f, 0f, null)

            // 5. Save final bitmap (no changes here)
            saveBitmapToMediaStore(context, finalBitmap)

            // 6. Delete original file (no changes here)
            rawImageUri.path?.let { path ->
                val file = File(path)
                if (file.exists()) file.delete()
            }

            // Success callback on main thread
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onError("Error combining image: ${e.message}")
            }
        }
    }
}

private suspend fun captureViewToBitmap(view: View): Bitmap =
    suspendCancellableCoroutine { continuation ->
        // Get the window of the view
        val window = (view.context as Activity).window

        // Create a bitmap with the same dimensions as the view
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

        // Get the location of the view in the window
        val locationOfViewInWindow = IntArray(2)
        view.getLocationInWindow(locationOfViewInWindow)
        val x = locationOfViewInWindow[0]
        val y = locationOfViewInWindow[1]
        val rect = Rect(x, y, x + view.width, y + view.height)

        // Create a PixelCopy listener
        val listener = PixelCopy.OnPixelCopyFinishedListener { copyResult ->
            if (copyResult == PixelCopy.SUCCESS) {
                continuation.resume(bitmap) { cause, _, _ -> }
            } else {
                continuation.cancel(IllegalStateException("PixelCopy failed with result: $copyResult"))
            }
        }

        // Request the pixel copy
        PixelCopy.request(window, rect, bitmap, listener, Handler(Looper.getMainLooper()))
    }

// Helper to decode and fix orientation
private fun decodeCameraBitmap(context: Context, uri: Uri): Bitmap {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IllegalStateException("Cannot open input stream")

    val original = BitmapFactory.decodeStream(inputStream)
    inputStream.close()

    val exif = ExifInterface(context.contentResolver.openInputStream(uri)!!)
    val rotationDegrees = when (exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }

    return if (rotationDegrees != 0f) {
        val matrix = Matrix().apply { postRotate(rotationDegrees) }
        createBitmap(original, 0, 0, original.width, original.height, matrix, true)
    } else original
}

private suspend fun saveBitmapToMediaStore(
    context: Context,
    bitmap: Bitmap
) = withContext(Dispatchers.IO) {
    val filename = "IMG_Overlay_${System.currentTimeMillis()}.jpg"

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/CameraXOverlay")
        }
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        ?: throw IllegalStateException("Failed to create MediaStore entry.")

    resolver.openOutputStream(uri).use { out ->
        if (out == null) throw IllegalStateException("Failed to open output stream")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
}