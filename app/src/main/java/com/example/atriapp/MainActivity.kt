package com.example.atriapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.atriapp.ui.theme.AtriAPPTheme
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.rememberCoroutineScope
import java.util.Date
import java.util.UUID
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import java.util.concurrent.TimeUnit

// Data Models
data class ComplaintData(
    val id: String = UUID.randomUUID().toString(),
    val category: String,
    val location: String,
    val description: String,
    val imageUri: String? = null,
    val timestamp: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
    val status: String = "Pending",
    val submittedBy: String = "rahul.s@atria.edu" // Default sender
)

data class ApiResponse(
    val success: Boolean,
    val message: String,
    val complaintId: String? = null
)

// Real API Interface
interface ComplaintApi {
    @POST("complaints")
    suspend fun submitComplaint(@Body complaint: ComplaintData): Response<ApiResponse>
    
    @GET("health")
    suspend fun testConnectivity(): Response<ApiResponse>
}

// API Service
class ComplaintApiService {
    companion object {
        // Production cloud server URL (will be updated after deployment)
        private const val BASE_URL = "https://your-app-name.onrender.com/api/"
        // For local testing: "http://10.0.2.2:3000/api/"
        // For network testing: "http://10.0.10.137:3000/api/"
        
        // Create Retrofit instance with logging
        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        private val api = retrofit.create(ComplaintApi::class.java)
        
        // Test connectivity to server
        suspend fun testConnectivity(): Boolean {
            return withContext(Dispatchers.IO) {
                try {
                    val response = api.testConnectivity()
                    response.isSuccessful
                } catch (e: Exception) {
                    println("Connectivity test failed: ${e.message}")
                    false
                }
            }
        }
        
        suspend fun submitComplaint(complaint: ComplaintData): ApiResponse {
            return withContext(Dispatchers.IO) {
                try {
                    // Real API call
                    val response = api.submitComplaint(complaint)
                    
                    if (response.isSuccessful) {
                        response.body() ?: ApiResponse(
                            success = false,
                            message = "Empty response from server"
                        )
                    } else {
                        ApiResponse(
                            success = false,
                            message = "Server error: ${response.code()} - ${response.message()}"
                        )
                    }
                } catch (e: Exception) {
                    ApiResponse(
                        success = false,
                        message = "Network error: ${e.message}"
                    )
                }
            }
        }
        
        // Get concerned person details based on category and sender
        fun getConcernedPerson(category: String, submittedBy: String = "rahul.s@atria.edu"): String {
            // Special routing: If complaint is from rahul.s@atria.edu, send to amaresh.k@atria.edu
            if (submittedBy == "rahul.s@atria.edu") {
                return "amaresh.k@atria.edu"
            }
            
            return when (category.lowercase()) {
                "maintenance" -> "maintenance@college.edu"
                "security" -> "security@college.edu"
                "building" -> "facilities@college.edu"
                "store" -> "store@college.edu"
                "housekeeping" -> "housekeeping@college.edu"
                else -> "admin@college.edu"
            }
        }
        
        // Get sender information
        fun getSenderInfo(): String {
            return "rahul.s@atria.edu"
        }
    }
}

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, camera can be used
        } else {
            // Permission denied
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AtriAPPTheme {
                AppNavigation()
            }
        }
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("home") }
    var selectedCategory by remember { mutableStateOf("") }

    when (currentScreen) {
        "home" -> {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                HomePage(
                    modifier = Modifier.padding(innerPadding),
                    onCategoryClick = { category ->
                        selectedCategory = category
                        currentScreen = "complaint_form"
                    }
                )
            }
        }
        "complaint_form" -> {
            ComplaintForm(
                category = selectedCategory,
                onBackClick = { currentScreen = "home" }
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ComplaintForm(category: String, onBackClick: () -> Unit) {
    var location by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var hasImage by remember { mutableStateOf(false) }
    var showCamera by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var submittedComplaintId by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Custom Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.Filled.ArrowBack, 
                        contentDescription = "Back",
                        tint = Color(0xFF4facfe)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Register Complaint - $category",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4facfe)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Concerned Person Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Sender Information
                    Text(
                        text = "From:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1976D2)
                    )
                    Text(
                        text = ComplaintApiService.getSenderInfo(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Recipient Information
                    Text(
                        text = "To:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1976D2)
                    )
                    Text(
                        text = ComplaintApiService.getConcernedPerson(category, ComplaintApiService.getSenderInfo()),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )
                }
            }

            // Location Field
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter the location of the issue") }
            )

            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                maxLines = 8,
                placeholder = { Text("Describe the issue in detail") }
            )

            // Image Attachment Section
            Text(
                text = "Attach Image",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4facfe)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FloatingActionButton(
                    onClick = { 
                        showCamera = true
                    },
                    modifier = Modifier.weight(1f),
                    containerColor = Color(0xFF4facfe),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Build, contentDescription = "Camera")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Camera")
                    }
                }

                FloatingActionButton(
                    onClick = { /* TODO: Open gallery */ },
                    modifier = Modifier.weight(1f),
                    containerColor = Color(0xFF00f2fe),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Build, contentDescription = "Gallery")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Gallery")
                    }
                }
            }

            // Show captured image if available
            capturedImageUri?.let { uri ->
                Text(
                    text = "✓ Image captured",
                    color = Color.Green,
                    fontSize = 14.sp
                )
                hasImage = true
            }

            if (hasImage) {
                Text(
                    text = "✓ Image attached",
                    color = Color.Green,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Submit Button
            Button(
                onClick = {
                    if (location.text.isNotBlank() && description.text.isNotBlank()) {
                        coroutineScope.launch {
                            submitComplaint(
                                category = category,
                                location = location.text,
                                description = description.text,
                                imageUri = capturedImageUri?.toString(),
                                context = context,
                                onStart = { isSubmitting = true },
                                onSuccess = { complaintId ->
                                    isSubmitting = false
                                    submittedComplaintId = complaintId
                                    showSuccessDialog = true
                                },
                                onError = { error ->
                                    isSubmitting = false
                                    errorMessage = error
                                    showErrorDialog = true
                                }
                            )
                        }
                    } else {
                        Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4facfe)),
                enabled = !isSubmitting
            ) {
                if (isSubmitting) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Submitting...")
                    }
                } else {
                    Text("Submit Complaint")
                }
            }
        }
    }

    // Camera UI
    if (showCamera) {
        CameraScreen(
            onImageCaptured = { uri ->
                capturedImageUri = uri
                hasImage = true
                showCamera = false
            },
            onError = { /* Handle error */ },
            onClose = { showCamera = false }
        )
    }

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Success!") },
            text = { 
                Column {
                    Text("Your complaint has been submitted successfully!")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Complaint ID: $submittedComplaintId")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("From: ${ComplaintApiService.getSenderInfo()}")
                    Text("To: ${ComplaintApiService.getConcernedPerson(category, ComplaintApiService.getSenderInfo())}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("The recipient has been notified.")
                }
            },
            confirmButton = {
                Button(
                    onClick = { 
                        showSuccessDialog = false
                        onBackClick() // Go back to home
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun CameraScreen(
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val preview: androidx.camera.core.Preview = remember { androidx.camera.core.Preview.Builder().build() }
    val cameraExecutor: Executor = remember { ContextCompat.getMainExecutor(context) }
    val outputDirectory: File = remember { context.getExternalFilesDir(null) ?: context.filesDir }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { previewView ->
                // Set up camera preview
                preview.setSurfaceProvider(previewView.surfaceProvider)
                
                // Bind camera use cases
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            context as androidx.lifecycle.LifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageCapture
                        )
                    } catch (e: Exception) {
                        // Handle camera binding error
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        )

        // Camera controls
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                ) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Close",
                        tint = Color.Black
                    )
                }

                IconButton(
                    onClick = {
                        takePhoto(
                            imageCapture = imageCapture,
                            outputDirectory = outputDirectory,
                            executor = cameraExecutor,
                            onImageCaptured = onImageCaptured,
                            onError = onError
                        )
                    },
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        Icons.Filled.Build,
                        contentDescription = "Take photo",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

private fun takePhoto(
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val photoFile = File(
        outputDirectory,
        SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                onError(exception)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                onImageCaptured(savedUri)
            }
        }
    )
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener(
            {
                continuation.resume(future.get())
            },
            ContextCompat.getMainExecutor(this)
        )
    }
}

@Composable
fun HomePage(modifier: Modifier = Modifier, onCategoryClick: (String) -> Unit = {}) {
    var isVisible by remember { mutableStateOf(false) }
    var showLogoAnimation by remember { mutableStateOf(true) }
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1500),
        label = "home_alpha"
    )

    // Scale animation
    val infiniteTransition = rememberInfiniteTransition(label = "logo_animation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = androidx.compose.animation.core.EaseInOut),
            repeatMode = androidx.compose.animation.core.RepeatMode.Restart
        ),
        label = "scale"
    )

    // Pulse animation
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2000
                0.8f at 0 with androidx.compose.animation.core.EaseInOut
                1.2f at 1000 with androidx.compose.animation.core.EaseInOut
                0.8f at 2000 with androidx.compose.animation.core.EaseInOut
            },
            repeatMode = androidx.compose.animation.core.RepeatMode.Restart
        ),
        label = "pulse"
    )

    LaunchedEffect(key1 = true) {
        kotlinx.coroutines.delay(300)
        isVisible = true
        kotlinx.coroutines.delay(3000) // Stop logo animation after 3 seconds
        showLogoAnimation = false
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4facfe),
                        Color(0xFF00f2fe),
                        Color(0xFF43e97b)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // College Logo (using atria.png as placeholder)
            Surface(shape = CircleShape, shadowElevation = 4.dp) {
                Image(
                    painter = painterResource(id = R.drawable.atria),
                    contentDescription = "College Logo",
                    modifier = Modifier
                        .height(96.dp)
                        .alpha(alpha)
                        .graphicsLayer(
                            scaleX = if (showLogoAnimation) (0.8f + scale * 0.4f) else 1f,
                            scaleY = if (showLogoAnimation) (0.8f + scale * 0.4f) else 1f,
                            alpha = if (showLogoAnimation) (0.7f + pulse * 0.3f) else 1f
                        )
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Welcome to Complaint Registration Portal",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.alpha(alpha)
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Complaint Categories Grid
            val categories = listOf(
                Category("Maintenance", Icons.Filled.Build),
                Category("Security", Icons.Filled.Build),
                Category("Building", Icons.Filled.Build),
                Category("Store", Icons.Filled.Build),
                Category("Housekeeping", Icons.Filled.Build)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .alpha(alpha),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories) { category ->
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { onCategoryClick(category.name) },
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.95f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .wrapContentHeight()
                                .wrapContentWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = category.icon,
                                contentDescription = category.name,
                                modifier = Modifier.height(40.dp),
                                tint = Color(0xFF4facfe)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = category.name,
                                color = Color(0xFF4facfe),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { /* TODO: Navigate to complaint registration */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .alpha(alpha),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4facfe))
            ) {
                Text("Register New Complaint")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO: Navigate to complaint history */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00f2fe)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .alpha(alpha),
            ) {
                Text("View My Complaints", color = Color.White)
            }
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedButton(
                onClick = { /* TODO: Handle logout */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .alpha(alpha),
            ) {
                Text("Logout")
            }
        }
    }
}

// Add this data class at the top-level (outside composables)
data class Category(val name: String, val icon: ImageVector)

// Submit Complaint Function
suspend fun submitComplaint(
    category: String,
    location: String,
    description: String,
    imageUri: String?,
    context: Context,
    onStart: () -> Unit,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    onStart()
    
    try {
        val complaint = ComplaintData(
            category = category,
            location = location,
            description = description,
            imageUri = imageUri,
            submittedBy = ComplaintApiService.getSenderInfo()
        )
        
        val response = ComplaintApiService.submitComplaint(complaint)
        
        if (response.success) {
            onSuccess(response.complaintId ?: complaint.id)
            
            // Show toast with concerned person info
            val concernedPerson = ComplaintApiService.getConcernedPerson(category, complaint.submittedBy)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "Complaint from ${complaint.submittedBy} sent to: $concernedPerson",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            onError(response.message)
        }
    } catch (e: Exception) {
        onError("Network error: ${e.message}")
    }
}