/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.capston_design;

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.Drawable
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.os.*
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.tensorflow.lite.examples.posenet.lib.BodyPart
import org.tensorflow.lite.examples.posenet.lib.Person
import org.tensorflow.lite.examples.posenet.lib.Posenet
import org.tensorflow.lite.examples.posenet.lib.Position
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.net.URL
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.round

class PosenetActivity :
  Fragment(),
  ActivityCompat.OnRequestPermissionsResultCallback {

  /** List of body joints that should be connected.    */
  private val bodyJoints = listOf(
          Pair(BodyPart.LEFT_WRIST, BodyPart.LEFT_ELBOW),
          Pair(BodyPart.LEFT_ELBOW, BodyPart.LEFT_SHOULDER),
          Pair(BodyPart.LEFT_SHOULDER, BodyPart.RIGHT_SHOULDER),
          Pair(BodyPart.RIGHT_SHOULDER, BodyPart.RIGHT_ELBOW),
          Pair(BodyPart.RIGHT_ELBOW, BodyPart.RIGHT_WRIST),
          Pair(BodyPart.LEFT_SHOULDER, BodyPart.LEFT_HIP),
          Pair(BodyPart.LEFT_HIP, BodyPart.RIGHT_HIP),
          Pair(BodyPart.RIGHT_HIP, BodyPart.RIGHT_SHOULDER),
          Pair(BodyPart.LEFT_HIP, BodyPart.LEFT_KNEE),
          Pair(BodyPart.LEFT_KNEE, BodyPart.LEFT_ANKLE),
          Pair(BodyPart.RIGHT_HIP, BodyPart.RIGHT_KNEE),
          Pair(BodyPart.RIGHT_KNEE, BodyPart.RIGHT_ANKLE)
  )

  private val bodypoint = listOf(
          Triple(BodyPart.LEFT_WRIST, BodyPart.LEFT_ELBOW, BodyPart.LEFT_SHOULDER),
          Triple(BodyPart.RIGHT_SHOULDER, BodyPart.RIGHT_ELBOW, BodyPart.RIGHT_WRIST),
          Triple(BodyPart.LEFT_HIP, BodyPart.LEFT_KNEE, BodyPart.LEFT_ANKLE),
          Triple(BodyPart.RIGHT_HIP, BodyPart.RIGHT_KNEE, BodyPart.RIGHT_ANKLE)
  )

  /** 한계값 for confidence score. */
  private val minConfidence = 0.5

  /** Radius of circle used to draw keypoints.  */
  private val circleRadius = 8.0f

  /** Paint class holds the style and color information to draw geometries,text and bitmaps. */
  private var paint = Paint()

  /** A shape for extracting frame data.   */
  private val PREVIEW_WIDTH = 640
  private val PREVIEW_HEIGHT = 480

  /** An object for the Posenet library.    */
  private lateinit var posenet: Posenet

  /** ID of the current [CameraDevice].   */
  private var cameraId: String? = null

  /** A [SurfaceView] for camera preview.   */
  private var surfaceView: SurfaceView? = null
  private lateinit var imageview: ImageView

  /** A [CameraCaptureSession] for camera preview.   */
  private var captureSession: CameraCaptureSession? = null

  /** A reference to the opened [CameraDevice].    */
  private var cameraDevice: CameraDevice? = null

  /** The [android.util.Size] of camera preview.  */
  private var previewSize: Size? = null

  /** The [android.util.Size.getWidth] of camera preview. */
  private var previewWidth = 0

  /** The [android.util.Size.getHeight] of camera preview.  */
  private var previewHeight = 0

  /** A counter to keep count of total frames.  */
  private var frameCounter = 0

  /** An IntArray to save image data in ARGB8888 format  */
  private lateinit var rgbBytes: IntArray
  private lateinit var rgbBytes2: IntArray

  /** A ByteArray to save image data in YUV format  */
  private var yuvBytes = arrayOfNulls<ByteArray>(3)

  /** An additional thread for running tasks that shouldn't block the UI.   */
  private var backgroundThread: HandlerThread? = null

  /** A [Handler] for running tasks in the background.    */
  private var backgroundHandler: Handler? = null

  /** An [ImageReader] that handles preview frame capture.   */
  private var imageReader: ImageReader? = null

  /** [CaptureRequest.Builder] for the camera preview   */
  private var previewRequestBuilder: CaptureRequest.Builder? = null

  /** [CaptureRequest] generated by [.previewRequestBuilder   */
  private var previewRequest: CaptureRequest? = null

  /** A [Semaphore] to prevent the app from exiting before closing the camera.    */
  private val cameraOpenCloseLock = Semaphore(1)

  /** Whether the current camera device supports Flash or not.    */
  private var flashSupported = false

  /** Orientation of the camera sensor.   */
  private var sensorOrientation: Int? = null

  /** Abstract interface to someone holding a display surface.    */
  private var surfaceHolder: SurfaceHolder? = null

  private var client = OkHttpClient()
  var pathlist: ArrayList<String> = ArrayList()
  var rightelbowlist : ArrayList<Double> = ArrayList()
  var leftelbowlist : ArrayList<Double> = ArrayList()
  var rightknee : ArrayList<Double> = ArrayList()
  var leftknee : ArrayList<Double> = ArrayList()
  private var IMAGEWEIGHT = 257
  private var IMAGEHEIGHT = 125
  private var yoganum = 0
  var videoAngleRIGHT_ELBOW = 0.0F
  var videoAngleLEFT_ELBOW = 0.0F
  var videoAngleRIGHT_KNEE = 0.0F
  var videoAngleLEFT_KNEE = 0.0F

  var errorRateRIGHT_ELBOW = 100.0F
  var errorRateLEFT_ELBOW = 100.0F
  var errorRateRIGHT_KNEE = 100.0F
  var errorRateLEFT_KNEE = 100.0F


  /** [CameraDevice.StateCallback] is called when [CameraDevice] changes its state.   */
  private val stateCallback = object : CameraDevice.StateCallback() {

    override fun onOpened(cameraDevice: CameraDevice) {
      cameraOpenCloseLock.release()
      this@PosenetActivity.cameraDevice = cameraDevice
      createCameraPreviewSession()
    }

    override fun onDisconnected(cameraDevice: CameraDevice) {
      cameraOpenCloseLock.release()
      cameraDevice.close()
      this@PosenetActivity.cameraDevice = null
    }

    override fun onError(cameraDevice: CameraDevice, error: Int) {
      onDisconnected(cameraDevice)
      this@PosenetActivity.activity?.finish()
    }
  }

  /**
   * A [CameraCaptureSession.CaptureCallback] that handles events related to JPEG capture.
   */
  private val captureCallback = object : CameraCaptureSession.CaptureCallback() {
    override fun onCaptureProgressed(
            session: CameraCaptureSession,
            request: CaptureRequest,
            partialResult: CaptureResult
    ) {
    }

    override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
    ) {
    }
  }

  /**
   * Shows a [Toast] on the UI thread.
   *
   * @param text The message to show
   */
  private fun showToast(text: String) {
    val activity = activity
    activity?.runOnUiThread { Toast.makeText(activity, text, Toast.LENGTH_SHORT).show() }
  }

  override fun onCreateView(
          inflater: LayoutInflater,
          container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View? = inflater.inflate(R.layout.tfe_pn_activity_posenet, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    surfaceView = view.findViewById(R.id.surfaceView)
    imageview = view.findViewById(R.id.surfaceView2)

    val display = requireActivity().windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)

    IMAGEWEIGHT = size.x
    IMAGEHEIGHT = (size.y*0.3).toInt()

    run("http://13.125.245.6:3000/api/yogas/getYogas?trimester=1st")
    surfaceHolder = surfaceView!!.holder
  }

  override fun onResume() {
    super.onResume()
    startBackgroundThread()
  }

  @SuppressLint("UseRequireInsteadOfGet")
  override fun onStart() {
    super.onStart()
    openCamera()
    posenet = Posenet(context = this.requireContext())
  }

  override fun onPause() {
    closeCamera()
    stopBackgroundThread()
    super.onPause()
  }

  override fun onDestroy() {
    super.onDestroy()
    posenet.close()
  }

  private fun requestCameraPermission() {
    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
      ConfirmationDialog().show(childFragmentManager, FRAGMENT_DIALOG)
    } else {
      requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
    }
  }

  override fun onRequestPermissionsResult(
          requestCode: Int,
          permissions: Array<String>,
          grantResults: IntArray
  ) {
    if (requestCode == REQUEST_CAMERA_PERMISSION) {
      if (allPermissionsGranted(grantResults)) {
        ErrorDialog.newInstance(getString(R.string.tfe_pn_request_permission))
                .show(childFragmentManager, FRAGMENT_DIALOG)
      }
    } else {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
  }

  private fun allPermissionsGranted(grantResults: IntArray) = grantResults.all {
    it == PackageManager.PERMISSION_GRANTED
  }

  /**
   * Sets up member variables related to camera.
   */
  private fun setUpCameraOutputs() {
    val activity = activity
    val manager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
    try {
      for (cameraId in manager.cameraIdList) {
        val characteristics = manager.getCameraCharacteristics(cameraId)

        // We don't use a front facing camera in this sample.
        val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
        if (cameraDirection != null &&
                cameraDirection == CameraCharacteristics.LENS_FACING_BACK
        ) {
          continue
        }

        previewSize = Size(PREVIEW_WIDTH, PREVIEW_HEIGHT)

        imageReader = ImageReader.newInstance(
                PREVIEW_WIDTH, PREVIEW_HEIGHT,
                ImageFormat.YUV_420_888, /*maxImages*/ 2
        )

        sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!

        previewHeight = previewSize!!.height
        previewWidth = previewSize!!.width

        // Initialize the storage bitmaps once when the resolution is known.
        rgbBytes = IntArray(previewWidth * previewHeight)

        // Check if the flash is supported.
        flashSupported =
                characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true

        this.cameraId = cameraId

        // We've found a viable camera and finished setting up member variables,
        // so we don't need to iterate through other available cameras.
        return
      }
    } catch (e: CameraAccessException) {
      Log.e(TAG, e.toString())
    } catch (e: NullPointerException) {
      // Currently an NPE is thrown when the Camera2API is used but not supported on the
      // device this code runs.
      ErrorDialog.newInstance(getString(R.string.tfe_pn_camera_error))
              .show(childFragmentManager, FRAGMENT_DIALOG)
    }
  }

  /**
   * Opens the camera specified by [PosenetActivity.cameraId].
   */
  private fun openCamera() {
    val permissionCamera = requireContext().checkPermission(
            Manifest.permission.CAMERA, Process.myPid(), Process.myUid()
    )
    if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
      requestCameraPermission()
    }
    setUpCameraOutputs()
    val manager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
    try {
      // Wait for camera to open - 2.5 seconds is sufficient
      if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
        throw RuntimeException("Time out waiting to lock camera opening.")
      }
      manager.openCamera(cameraId!!, stateCallback, backgroundHandler)
    } catch (e: CameraAccessException) {
      Log.e(TAG, e.toString())
    } catch (e: InterruptedException) {
      throw RuntimeException("Interrupted while trying to lock camera opening.", e)
    }
  }

  /**
   * Closes the current [CameraDevice].
   */
  private fun closeCamera() {
    if (captureSession == null) {
      return
    }

    try {
      cameraOpenCloseLock.acquire()
      captureSession!!.close()
      captureSession = null
      cameraDevice!!.close()
      cameraDevice = null
      imageReader!!.close()
      imageReader = null
    } catch (e: InterruptedException) {
      throw RuntimeException("Interrupted while trying to lock camera closing.", e)
    } finally {
      cameraOpenCloseLock.release()
    }
  }

  /**
   * Starts a background thread and its [Handler].
   */
  private fun startBackgroundThread() {
    backgroundThread = HandlerThread("imageAvailableListener").also { it.start() }
    backgroundHandler = Handler(backgroundThread!!.looper)
  }

  /**
   * Stops the background thread and its [Handler].
   */
  private fun stopBackgroundThread() {
    backgroundThread?.quitSafely()
    try {
      backgroundThread?.join()
      backgroundThread = null
      backgroundHandler = null
    } catch (e: InterruptedException) {
      Log.e(TAG, e.toString())
    }
  }

  /** Fill the yuvBytes with data from image planes.   */
  private fun fillBytes(planes: Array<Image.Plane>, yuvBytes: Array<ByteArray?>) {
    // Row stride is the total number of bytes occupied in memory by a row of an image.
    // Because of the variable row stride it's not possible to know in
    // advance the actual necessary dimensions of the yuv planes.
    for (i in planes.indices) {
      val buffer = planes[i].buffer
      if (yuvBytes[i] == null) {
        yuvBytes[i] = ByteArray(buffer.capacity())
      }
      buffer.get(yuvBytes[i]!!)
    }
  }

  /** A [OnImageAvailableListener] to receive frames as they are available.  */
  private var imageAvailableListener = object : OnImageAvailableListener {
    override fun onImageAvailable(imageReader: ImageReader) {
      // We need wait until we have some size from onPreviewSizeChosen
      if (previewWidth == 0 || previewHeight == 0) {
        return
      }

      val image = imageReader.acquireLatestImage() ?: return
      fillBytes(image.planes, yuvBytes)

      ImageUtils.convertYUV420ToARGB8888(
              yuvBytes[0]!!,
              yuvBytes[1]!!,
              yuvBytes[2]!!,
              previewWidth,
              previewHeight,
              /*yRowStride=*/ image.planes[0].rowStride,
              /*uvRowStride=*/ image.planes[1].rowStride,
              /*uvPixelStride=*/ image.planes[1].pixelStride,
              rgbBytes
      )

      // Create bitmap from int array
      val imageBitmap = Bitmap.createBitmap(
              rgbBytes, previewWidth, previewHeight,
              Bitmap.Config.ARGB_8888
      )

      // Create rotated version for portrait display
      val rotateMatrix = Matrix()
      rotateMatrix.postRotate(270.0f)

      val rotatedBitmap = Bitmap.createBitmap(
              imageBitmap, 0, 0, previewWidth, previewHeight,
              rotateMatrix, true
      )
      image.close()

      processImage(rotatedBitmap)
    }
  }


  /** Crop Bitmap to maintain aspect ratio of model input.   */
  private fun cropBitmap(bitmap: Bitmap): Bitmap {
    val bitmapRatio = bitmap.height.toFloat() / bitmap.width
    val modelInputRatio = MODEL_HEIGHT.toFloat() / MODEL_WIDTH
    var croppedBitmap = bitmap

    // Acceptable difference between the modelInputRatio and bitmapRatio to skip cropping.
    val maxDifference = 1e-5

    // Checks if the bitmap has similar aspect ratio as the required model input.
    when {
      abs(modelInputRatio - bitmapRatio) < maxDifference -> return croppedBitmap
      modelInputRatio < bitmapRatio -> {
        // New image is taller so we are height constrained.
        val cropHeight = bitmap.height - (bitmap.width.toFloat() / modelInputRatio)
        croppedBitmap = Bitmap.createBitmap(
                bitmap,
                0,
                (cropHeight / 2).toInt(),
                bitmap.width,
                (bitmap.height - cropHeight).toInt()
        )
      }
      else -> {
        val cropWidth = bitmap.width - (bitmap.height.toFloat() * modelInputRatio)
        croppedBitmap = Bitmap.createBitmap(
                bitmap,
                (cropWidth / 2).toInt(),
                0,
                (bitmap.width - cropWidth).toInt(),
                bitmap.height
        )
      }
    }
    return croppedBitmap
  }



  /** Set the paint color and size.    */
  private fun setPaint() {
    paint.color = Color.RED
    paint.textSize = 80.0f
    paint.strokeWidth = 8.0f
  }

  /** Draw bitmap on Canvas.   */
  private fun draw(canvas: Canvas, person: Person, bitmap: Bitmap) {
    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    // Draw `bitmap` and `person` in square canvas.
    val screenWidth: Int
    val screenHeight: Int
    val left: Int
    val right: Int
    val top: Int
    val bottom: Int

    if (canvas.height > canvas.width) {
      screenWidth = canvas.width
      screenHeight = canvas.width
      left = 0
      top = (canvas.height - canvas.width) / 2
    } else {
      screenWidth = canvas.height
      screenHeight = canvas.height
      left = (canvas.width - canvas.height) / 2
      top = 0
    }
    right = left + screenWidth
    bottom = top + screenHeight

    val matrix = Matrix()
    matrix.preScale(-1.0f, 1.0f)
    val resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)

    setPaint()
    canvas.drawBitmap(
            resizedBitmap,
            Rect(0, 0, bitmap.width, bitmap.height),
            Rect(left, 0, right, bottom),
            paint
    )

    val widthRatio = screenWidth.toFloat() / MODEL_WIDTH
    val heightRatio = screenHeight.toFloat() / MODEL_HEIGHT

    // Draw key points over the image.
    var i = 0
    var videochecks : MutableList<Boolean> = mutableListOf(false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false)
    val videoPositions = Array(17) { Position() }

    for (i in 0..person.keyPoints.size - 1) {
      if (person.keyPoints[i].score > minConfidence) {
        //imageKeyPoints[i].bodyPart = person.keyPoints[i].bodyPart
        //imageKeyPoints[i].position = person.keyPoints[i].position
        videochecks[i]=true
        val position = person.keyPoints[i].position
        val adjustedX: Float = screenWidth - (position.x * widthRatio + left)
        val adjustedY: Float = position.y * heightRatio + top/2
        videoPositions[i].x = adjustedX
        videoPositions[i].y = adjustedY
        canvas.drawCircle(adjustedX, adjustedY, circleRadius, paint)
      }

    }

    for (line in bodyJoints) {
      if (
              (person.keyPoints[line.first.ordinal].score > minConfidence) and
              (person.keyPoints[line.second.ordinal].score > minConfidence)
      ) {
        canvas.drawLine(
                screenWidth - (person.keyPoints[line.first.ordinal].position.x.toFloat() * widthRatio + left),
                person.keyPoints[line.first.ordinal].position.y.toFloat() * heightRatio + top/2,
                screenWidth - (person.keyPoints[line.second.ordinal].position.x.toFloat() * widthRatio + left),
                person.keyPoints[line.second.ordinal].position.y.toFloat() * heightRatio + top/2,
                paint
        )

      }
    }
/*

    for (line in bodypoint) {
      if (
              (person.keyPoints[line.first.ordinal].score > minConfidence) and
              (person.keyPoints[line.second.ordinal].score > minConfidence) and
              (person.keyPoints[line.third.ordinal].score > minConfidence)
      ) {
        getAngle(person.keyPoints[line.first.ordinal].position,
                person.keyPoints[line.second.ordinal].position,
                person.keyPoints[line.third.ordinal].position)
      }
    }
*/

    videoAngleRIGHT_ELBOW = 0.0F
    videoAngleLEFT_ELBOW = 0.0F
    videoAngleRIGHT_KNEE = 0.0F
    videoAngleLEFT_KNEE = 0.0F

    errorRateRIGHT_ELBOW = 100.0F
    errorRateLEFT_ELBOW = 100.0F
    errorRateRIGHT_KNEE = 100.0F
    errorRateLEFT_KNEE = 100.0F

    if (videochecks[6] and videochecks[8] and videochecks[10]){
      videoAngleRIGHT_ELBOW = getAngle(videoPositions[6], videoPositions[8], videoPositions[10])
      errorRateRIGHT_ELBOW = (round((abs(rightelbowlist[yoganum] - videoAngleRIGHT_ELBOW)) / rightelbowlist[yoganum] * 100) * 10 / 10).toFloat()
    }
    if (videochecks[5] and videochecks[7] and videochecks[9]){
      videoAngleLEFT_ELBOW = getAngle(videoPositions[5], videoPositions[7], videoPositions[9])
      errorRateLEFT_ELBOW = (round((abs(leftelbowlist[yoganum] - videoAngleLEFT_ELBOW)) / leftelbowlist[yoganum] * 100) * 10 / 10).toFloat()
    }
    if (videochecks[12] and videochecks[14] and videochecks[16]){
      videoAngleRIGHT_KNEE = getAngle(videoPositions[12], videoPositions[14], videoPositions[16])
      errorRateRIGHT_KNEE = (round((abs(leftelbowlist[yoganum] - videoAngleRIGHT_KNEE)) / leftelbowlist[yoganum] * 100) * 10 / 10).toFloat()
    }
    if (videochecks[11] and videochecks[13] and videochecks[15]){
      videoAngleLEFT_KNEE = getAngle(videoPositions[11], videoPositions[13], videoPositions[15])
      errorRateLEFT_KNEE= (round((abs(leftelbowlist[yoganum] - videoAngleLEFT_KNEE)) / leftelbowlist[yoganum] * 100) * 10 / 10).toFloat()
    }

    if ((errorRateRIGHT_ELBOW < 20) or (errorRateLEFT_ELBOW < 20) or (errorRateRIGHT_KNEE < 20) or (errorRateLEFT_KNEE < 20)){
      Log.e("다음", "넘어가세요")

      nextimage()

    }
    // Draw!
    surfaceHolder!!.unlockCanvasAndPost(canvas)
  }

  /** Process image using Posenet library.   */
  private fun processImage(bitmap: Bitmap) {
    // Crop bitmap.
    //val croppedBitmap = cropBitmap(bitmap)

    // Created scaled version of bitmap for model input.
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, MODEL_WIDTH, MODEL_HEIGHT, true)

    // Perform inference.
    val person = posenet.estimateSinglePose(scaledBitmap)
    val canvas: Canvas = surfaceHolder!!.lockCanvas()
    draw(canvas, person, scaledBitmap)
  }

  /**
   * Creates a new [CameraCaptureSession] for camera preview.
   */
  private fun createCameraPreviewSession() {
    try {
      // We capture images from preview in YUV format.
      imageReader = ImageReader.newInstance(
              previewSize!!.width, previewSize!!.height, ImageFormat.YUV_420_888, 2
      )
      imageReader!!.setOnImageAvailableListener(imageAvailableListener, backgroundHandler)

      // This is the surface we need to record images for processing.
      val recordingSurface = imageReader!!.surface

      // We set up a CaptureRequest.Builder with the output Surface.
      previewRequestBuilder = cameraDevice!!.createCaptureRequest(
              CameraDevice.TEMPLATE_PREVIEW
      )
      previewRequestBuilder!!.addTarget(recordingSurface)

      // Here, we create a CameraCaptureSession for camera preview.
      cameraDevice!!.createCaptureSession(
              listOf(recordingSurface),
              object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                  // The camera is already closed
                  if (cameraDevice == null) return

                  // When the session is ready, we start displaying the preview.
                  captureSession = cameraCaptureSession
                  try {
                    // Auto focus should be continuous for camera preview.
                    previewRequestBuilder!!.set(
                            CaptureRequest.CONTROL_AF_MODE,
                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                    )
                    // Flash is automatically enabled when necessary.
                    setAutoFlash(previewRequestBuilder!!)

                    // Finally, we start displaying the camera preview.
                    previewRequest = previewRequestBuilder!!.build()
                    captureSession!!.setRepeatingRequest(
                            previewRequest!!,
                            captureCallback, backgroundHandler
                    )
                  } catch (e: CameraAccessException) {
                    Log.e(TAG, e.toString())
                  }
                }

                override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                  showToast("Failed")
                }
              },
              null
      )
    } catch (e: CameraAccessException) {
      Log.e(TAG, e.toString())
    }
  }

  private fun setAutoFlash(requestBuilder: CaptureRequest.Builder) {
    if (flashSupported) {
      requestBuilder.set(
              CaptureRequest.CONTROL_AE_MODE,
              CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
      )
    }
  }

  /**
   * Shows an error message dialog.
   */
  class ErrorDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity)
                    .setMessage(arguments!!.getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok) { _, _ -> activity!!.finish() }
                    .create()

    companion object {

      @JvmStatic
      private val ARG_MESSAGE = "message"

      @JvmStatic
      fun newInstance(message: String): ErrorDialog = ErrorDialog().apply {
        arguments = Bundle().apply { putString(ARG_MESSAGE, message) }
      }
    }
  }

  companion object {
    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private val ORIENTATIONS = SparseIntArray()
    private val FRAGMENT_DIALOG = "dialog"


    init {
      ORIENTATIONS.append(Surface.ROTATION_0, 90)
      ORIENTATIONS.append(Surface.ROTATION_90, 0)
      ORIENTATIONS.append(Surface.ROTATION_180, 270)
      ORIENTATIONS.append(Surface.ROTATION_270, 180)
    }

    /**
     * Tag for the [Log].
     */
    private const val TAG = "PosenetActivity"
  }


  fun run(url: String) {
    val request = Request.Builder().url(url).build()


    client.newCall(request).enqueue(object : Callback {
      override fun onFailure(call: Call, e: IOException) {
        Log.e("result", e.toString())
      }

      override fun onResponse(call: Call, response: Response) {
        var str_reponse = response.body()!!.string()

        val json_contact: JSONObject = JSONObject(str_reponse)
        var jsonarray_info: JSONArray = json_contact.getJSONArray("res_data")
        var i: Int = 0
        var size: Int = jsonarray_info.length()

        for (i in 0..size - 1) {
          var json_ob: JSONObject = jsonarray_info.getJSONObject(i)
          var path: String = json_ob.get("path") as String
          var relbow: Double = json_ob.get("imageAngleRIGHT_ELBOW") as Double
          var lelbow: Double = json_ob.get("imageAngleLEFT_ELBOW") as Double
          var rknee: Double = json_ob.get("imageAngleRIGHT_KNEE") as Double
          var lknee: Double = json_ob.get("imageAngleLEFT_KNEE") as Double
          pathlist.add(path)
          rightelbowlist.add(relbow)
          leftelbowlist.add(lelbow)
          rightknee.add(rknee)
          leftknee.add(lknee)
          Log.e("json", json_ob.toString())
        }

        setImage(pathlist.get(yoganum))

      }
    })
  }

  fun setImage(url: String) {

    Glide.with(this).asBitmap().load(url).into(object : CustomTarget<Bitmap>(){
      override fun onLoadCleared(placeholder: Drawable?) {

      }

      override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        imageview.setImageBitmap(resource)
      }
    })
  }


  fun getAngle(a: Position, b: Position, c: Position): Float {
    val dx1: Double = (c.x - b.x).toDouble() // x 변화량, b -> c
    val dx2: Double = (a.x - b.x).toDouble() // x 변화량, b -> a
    val dy1: Double = (c.y - b.y).toDouble() // y 변화량, b -> c
    val dy2: Double = (a.y - b.y).toDouble() // y 변화량, b -> a

    val radians = Math.abs(Math.atan2(dy1, dx1) - Math.atan2(dy2, dx2))
    val angle : Float = Math.toDegrees(radians).toFloat()

    Log.e("angle : ",angle.toString())

    return angle
  }

  fun nextimage(){
    Toast.makeText(context,"자세를 유지하세요",Toast.LENGTH_SHORT).show()
    Toast.makeText(context,"다음 자세로 넘어갑니다.",Toast.LENGTH_SHORT).show()

    videoAngleRIGHT_ELBOW = 0.0F
    videoAngleLEFT_ELBOW = 0.0F
    videoAngleRIGHT_KNEE = 0.0F
    videoAngleLEFT_KNEE = 0.0F

    errorRateRIGHT_ELBOW = 100.0F
    errorRateLEFT_ELBOW = 100.0F
    errorRateRIGHT_KNEE = 100.0F
    errorRateLEFT_KNEE = 100.0F


    Handler().postDelayed({

      //yoganum +=1
      setImage(pathlist.get(1))

    },4000)
  }

}
