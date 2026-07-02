package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    authMode: String, // "LOGIN" or "REGISTER"
    onSelectMode: (String) -> Unit,
    onLogin: (String, String) -> Unit,
    onRegister: (String, String, String, String, String, Float, Float, String, String, String, String, String, String) -> Unit,
    loginError: String?,
    firebaseSyncStatus: String,
    modifier: Modifier = Modifier
) {
    var memberIdInput by remember { mutableStateOf("SLAM-8849") }
    var dobInput by remember { mutableStateOf("14 Aug 1996") }

    // Register form fields
    var regName by remember { mutableStateOf("Vignesh Gnanashankar") }
    var regId by remember { mutableStateOf("SLAM-9920") }
    var regDob by remember { mutableStateOf("20-05-1998") }
    var regMobile by remember { mutableStateOf("+91 98402 11988") }
    var regEmail by remember { mutableStateOf("vignesh@slam.app") }
    var regWeight by remember { mutableStateOf("75.0") }
    var regHeight by remember { mutableStateOf("176.0") }
    var regBodyMeasurements by remember { mutableStateOf("Chest: 40\", Waist: 32\", Biceps: 15\"") }
    var regBloodGroup by remember { mutableStateOf("O+") }
    var regGender by remember { mutableStateOf("Male") }
    var regGoal by remember { mutableStateOf("Muscle Gain & Athletic Endurance") }
    var regHealth by remember { mutableStateOf("None") }
    var regRole by remember { mutableStateOf("MEMBER") } // "MEMBER", "TRAINER", "ADMIN"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlamObsidian)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            // SLAM Villivakkam Hero Banner Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(2.dp, SlamCrimson)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_slam_red_hero),
                        contentDescription = "SLAM Villivakkam Hero",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Badge(containerColor = SlamCrimson, contentColor = Color.White) {
                                Text("VILLIVAKKAM BRANCH HQ", fontWeight = FontWeight.Black, fontSize = 10.sp)
                            }
                            Badge(containerColor = Color(0xFF1B5E20), contentColor = Color.White) {
                                Text("CLOUD SYNCED", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = androidx.compose.ui.text.buildAnnotatedString {
                                withStyle(androidx.compose.ui.text.SpanStyle(color = Color.White)) { append("S") }
                                withStyle(androidx.compose.ui.text.SpanStyle(color = SlamCrimson)) { append("L") }
                                withStyle(androidx.compose.ui.text.SpanStyle(color = Color.White)) { append("AM LIFESTYLE & FITNESS") }
                            },
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "7,000 SQ.FT PRO STUDIO • CHENNAI",
                            style = MaterialTheme.typography.labelMedium.copy(color = ElectricLime, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // Firebase Database Realtime Status Badge
            Surface(
                color = SlamSurface,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SlamBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        Icons.Default.CloudSync,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text("FIREBASE CLOUD DATABASE", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 9.sp))
                        Text(
                            text = firebaseSyncStatus,
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                        )
                    }
                }
            }

            // Tab Switcher (LOGIN vs REGISTER)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SlamSurfaceElevated, RoundedCornerShape(16.dp))
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val isLogin = authMode == "LOGIN"
                Button(
                    onClick = { onSelectMode("LOGIN") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLogin) SlamCrimson else Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("MEMBER LOGIN", fontWeight = FontWeight.Black)
                }

                Button(
                    onClick = { onSelectMode("REGISTER") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isLogin) SlamCrimson else Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("NEW REGISTER", fontWeight = FontWeight.Black)
                }
            }

            // Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlamSurface),
                shape = RoundedCornerShape(22.dp),
                border = BorderStroke(1.dp, SlamBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (loginError != null) {
                        Surface(
                            color = Color(0xFF3B1014),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, SlamCrimson)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = SlamRedBright)
                                Text(loginError, style = MaterialTheme.typography.bodySmall.copy(color = Color.White))
                            }
                        }
                    }

                    if (authMode == "LOGIN") {
                        Text(
                            "ACCESS SLAM MEMBER PORTAL",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = Color.White)
                        )
                        Text(
                            "Enter your Membership ID and Date of Birth verified against our Firebase member registry.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )

                        OutlinedTextField(
                            value = memberIdInput,
                            onValueChange = { memberIdInput = it },
                            label = { Text("Membership ID") },
                            placeholder = { Text("e.g. SLAM-8849") },
                            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = SlamCrimson) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SlamCrimson,
                                unfocusedBorderColor = SlamBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        OutlinedTextField(
                            value = dobInput,
                            onValueChange = { dobInput = it },
                            label = { Text("Date of Birth (DOB)") },
                            placeholder = { Text("e.g. 14 Aug 1996") },
                            leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = SlamCrimson) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SlamCrimson,
                                unfocusedBorderColor = SlamBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        Button(
                            onClick = { onLogin(memberIdInput, dobInput) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.LockOpen, contentDescription = null)
                                Text("SIGN IN TO SLAM STUDIO", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black))
                            }
                        }

                        // Quick Access Role Fillers
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { memberIdInput = "SLAM-8849"; dobInput = "14 Aug 1996" },
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, SlamBorder),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                            ) {
                                Text("Member", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = { memberIdInput = "TRAINER-101"; dobInput = "01 Jan 1990" },
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, ElectricLime),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricLime)
                            ) {
                                Text("Trainer", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = { memberIdInput = "ADMIN-SLAM"; dobInput = "01 Jan 1985" },
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, SlamCrimson),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = SlamRedBright)
                            ) {
                                Text("Admin", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Demo Tip
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SlamSurfaceElevated, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = ElectricLime, modifier = Modifier.size(18.dp))
                            Text(
                                "Tip: Click Member, Trainer, or Admin buttons above for quick role access, or register a new profile.",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, lineHeight = 16.sp)
                            )
                        }
                    } else {
                        // REGISTER FORM
                        Text(
                            "CREATE NEW MEMBER PROFILE",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = Color.White)
                        )

                        OutlinedTextField(
                            value = regName,
                            onValueChange = { regName = it },
                            label = { Text("Full Name") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SlamCrimson) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = regId,
                                onValueChange = { regId = it },
                                label = { Text("Desired Member ID") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = regDob,
                                onValueChange = { regDob = it },
                                label = { Text("DOB") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        OutlinedTextField(
                            value = regMobile,
                            onValueChange = { regMobile = it },
                            label = { Text("Contact Mobile") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = SlamCrimson) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = regEmail,
                            onValueChange = { regEmail = it },
                            label = { Text("Email Address") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = SlamCrimson) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = regWeight,
                                onValueChange = { regWeight = it },
                                label = { Text("Weight (kg)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = regHeight,
                                onValueChange = { regHeight = it },
                                label = { Text("Height (cm)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = regBloodGroup,
                                onValueChange = { regBloodGroup = it },
                                label = { Text("Blood Group") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = regGender,
                                onValueChange = { regGender = it },
                                label = { Text("Gender") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        OutlinedTextField(
                            value = regBodyMeasurements,
                            onValueChange = { regBodyMeasurements = it },
                            label = { Text("Body Measurements (Chest/Waist/Arms)") },
                            leadingIcon = { Icon(Icons.Default.Straighten, contentDescription = null, tint = SlamCrimson) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = regGoal,
                            onValueChange = { regGoal = it },
                            label = { Text("Primary Fitness Goal") },
                            leadingIcon = { Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = SlamCrimson) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = regHealth,
                            onValueChange = { regHealth = it },
                            label = { Text("Health Issues / Medical Notes") },
                            leadingIcon = { Icon(Icons.Default.MedicalInformation, contentDescription = null, tint = SlamCrimson) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Text("Select Account Role Type:", style = MaterialTheme.typography.labelMedium.copy(color = Color.White, fontWeight = FontWeight.Bold))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("MEMBER", "TRAINER", "ADMIN").forEach { role ->
                                val isSelected = regRole == role
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { regRole = role },
                                    label = { Text(role, fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = SlamCrimson,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        Button(
                            onClick = {
                                val w = regWeight.toFloatOrNull() ?: 75f
                                val h = regHeight.toFloatOrNull() ?: 176f
                                onRegister(regName, regId, regDob, regMobile, regEmail, w, h, regBodyMeasurements, regBloodGroup, regGender, regGoal, regHealth, regRole)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SlamCrimson),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("CREATE & SYNC TO FIREBASE", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black))
                        }
                    }
                }
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}
