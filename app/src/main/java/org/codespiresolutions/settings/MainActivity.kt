// ©CodeSpire-Solutions - This Code is under the copyright of CodeSpire-Solutions. Any Usage without our Permission will be suited

package org.codespiresolutions.settings

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.navigation.compose.NavHost
import org.codespiresolutions.settings.ui.theme.SettingsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val wifiSsid = getCurrentWifiSsid()

        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) } // Default to light theme

            SettingsTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "settings_screen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("settings_screen") {
                            SettingsScreen(
                                wifiSsid = wifiSsid,
                                onToggleTheme = { isDarkTheme = !isDarkTheme },
                                navController = navController
                            )
                        }
                        composable("wifi_screen") {
                            WifiScreen()
                        }
                        composable("bluetooth_screen") {
                            BluetoothScreen()
                        }
                        composable("developer_options_screen") {
                            DeveloperOptionsScreen(isDarkTheme = isDarkTheme)
                        }
                    }
                }
            }
        }
    }

    /**
     * Get the current Wi-Fi SSID.
     */
    @SuppressLint("ObsoleteSdkInt")
    private fun getCurrentWifiSsid(): String {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            wifiManager.connectionInfo.ssid?.replace("\"", "") ?: "Not Connected"
        } else {
            wifiManager.connectionInfo.ssid?.replace("\"", "") ?: "Not Connected"
        }
    }
}

val Figtree = FontFamily(
    Font(R.font.figtree_regular, FontWeight.Normal),
    Font(R.font.figtree_bold, FontWeight.Bold)
)

val CustomTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Figtree,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
)

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    wifiSsid: String,
    onToggleTheme: () -> Unit,
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Settings",
            style = CustomTypography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SearchBar(query = searchQuery, onQueryChanged = { searchQuery = it })

        Spacer(modifier = Modifier.height(16.dp))

        SettingsOption(
            title = "Wi-Fi",
            subtitle = "Mobile, Wi-Fi, Hotspot",
            iconResId = R.drawable.wifi,
            onClick = { navController.navigate("wifi_screen") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsOption(
            title = "Bluetooth",
            subtitle = "Manage Bluetooth",
            iconResId = R.drawable.bluetooth,
            onClick = { navController.navigate("bluetooth_screen") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsOption(
            title = "Developer Options",
            subtitle = "Manage advanced settings",
            iconResId = R.drawable.developer_options,
            onClick = { navController.navigate("developer_options_screen") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsOption(
            title = "Toggle Theme",
            subtitle = "Switch between light and dark theme",
            iconResId = R.drawable.display,
            onClick = { onToggleTheme() } // Toggle theme
        )
    }
}

@Composable
fun SettingsOption(title: String, subtitle: String, iconResId: Int, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Text Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = CustomTypography.bodyLarge.copy(fontSize = 35.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = CustomTypography.bodyLarge.copy(
						fontSize = 16.sp, // Schriftgröße auf 16 ändern
						color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
					),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        label = { Text(text = "Search", color = MaterialTheme.colorScheme.onBackground) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    )
}

// Wi-Fi Screen Composable
@Composable
fun WifiScreen() {
    // Retrieve current Wi-Fi SSID and signal strength
    val wifiInfo = getCurrentWifiInfo()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Wi-Fi Settings",
            style = CustomTypography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Current Wi-Fi Network Info
        WifiNetworkInfo(wifiInfo)

        Spacer(modifier = Modifier.height(16.dp))

        // List of Available Networks (Placeholder for now)
        AvailableWifiNetworks()

        Spacer(modifier = Modifier.height(16.dp))

        // Button to Toggle Wi-Fi (Optional)
        WifiToggleButton()
    }
}

// Display the current connected Wi-Fi SSID and signal strength
@Composable
fun WifiNetworkInfo(wifiInfo: WifiInfo?) {
    if (wifiInfo == null) {
        Text(text = "No Wi-Fi connection detected.", style = CustomTypography.bodyLarge)
    } else {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Connected to:",
                style = CustomTypography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = wifiInfo.ssid ?: "N/A",
                style = CustomTypography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Signal strength (Display as bars)
            SignalStrengthIndicator(wifiInfo.rssi)
        }
    }
}

// Signal strength indicator using bars
@Composable
fun SignalStrengthIndicator(rssi: Int) {
    val signalStrength = when {
        rssi >= -50 -> "Excellent"
        rssi >= -60 -> "Good"
        rssi >= -70 -> "Fair"
        else -> "Poor"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.signal),
            contentDescription = "Signal Strength",
            tint = Color.Green,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Signal Strength: $signalStrength",
            style = CustomTypography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

// Placeholder for available Wi-Fi networks (just a list of networks)
@Composable
fun AvailableWifiNetworks() {
    // Sample Wi-Fi networks list (replace with actual data if necessary)
    val availableNetworks = listOf("Network1", "Network2", "Network3")

    Text(
        text = "Available Networks:",
        style = CustomTypography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        availableNetworks.forEach { network ->
            Text(
                text = network,
                style = CustomTypography.bodyLarge.copy(color = Color.Gray),
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

// Button to toggle Wi-Fi (Optional)
@Composable
fun WifiToggleButton() {
    Button(
        onClick = { /* TODO: Implement Wi-Fi toggle functionality */ },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = "Toggle Wi-Fi")
    }
}

// Fetch current Wi-Fi Info (SSID and signal strength)
@Composable
@SuppressLint("MissingPermission")
fun getCurrentWifiInfo(): WifiInfo? {
    val wifiManager = LocalContext.current.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    return wifiManager.connectionInfo
}

@Preview(showBackground = true)
@Composable
fun WifiScreenPreview() {
    SettingsTheme {
        WifiScreen()
    }
}

@SuppressLint("MissingPermission")
@Composable
fun BluetoothScreen() {
    var isBluetoothEnabled by remember { mutableStateOf(false) }
    var pairedDevices by remember { mutableStateOf<List<BluetoothDevice>>(emptyList()) }

    // Initialize BluetoothAdapter and check if Bluetooth is enabled
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    val context = LocalContext.current

    // Register a receiver to listen for Bluetooth device discoveries and pairing
    val bluetoothReceiver = rememberUpdatedState(BluetoothReceiver(context, onDeviceFound = { device ->
        // Add paired device to list
        if (device.bondState == BluetoothDevice.BOND_BONDED) {
            pairedDevices = pairedDevices + device
        }
    }))

    DisposableEffect(context) {
        val filter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        context.registerReceiver(bluetoothReceiver.value, filter)
        onDispose {
            context.unregisterReceiver(bluetoothReceiver.value)
        }
    }

    // Get Bluetooth status
    isBluetoothEnabled = bluetoothAdapter?.isEnabled == true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Bluetooth Settings",
            style = CustomTypography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Bluetooth Status
        BluetoothStatus(isBluetoothEnabled)

        Spacer(modifier = Modifier.height(16.dp))

        // Paired Devices
        PairedDevicesList(pairedDevices)

        Spacer(modifier = Modifier.height(16.dp))

        // Button to Toggle Bluetooth
        BluetoothToggleButton(isBluetoothEnabled) { toggleBluetooth() }
    }
}

@Composable
fun BluetoothStatus(isBluetoothEnabled: Boolean) {
    Text(
        text = if (isBluetoothEnabled) "Bluetooth is ON" else "Bluetooth is OFF",
        style = CustomTypography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@SuppressLint("MissingPermission")
@Composable
fun PairedDevicesList(pairedDevices: List<BluetoothDevice>) {
    Text(
        text = "Paired Devices:",
        style = CustomTypography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        pairedDevices.forEach { device ->
            Text(
                text = device.name ?: "Unknown Device",
                style = CustomTypography.bodyLarge.copy(color = Color.Gray),
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun BluetoothToggleButton(isBluetoothEnabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = if (isBluetoothEnabled) "Disable Bluetooth" else "Enable Bluetooth")
    }
}

// Toggle Bluetooth state
@SuppressLint("MissingPermission")
fun toggleBluetooth() {
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    bluetoothAdapter?.let {
        if (it.isEnabled) {
            it.disable() // Turn off Bluetooth
        } else {
            it.enable() // Turn on Bluetooth
        }
    }
}

// BroadcastReceiver for Bluetooth state changes
class BluetoothReceiver(
    private val context: Context,
    private val onDeviceFound: (BluetoothDevice) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == action) {
            val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
            onDeviceFound(device)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BluetoothScreenPreview() {
    SettingsTheme {
        BluetoothScreen()
    }
}

@Composable
fun DeveloperOptionsScreen(isDarkTheme: Boolean) {
    // State variables to control the toggles
    var isDeveloperModeEnabled by remember { mutableStateOf(false) }
    var isLoggingEnabled by remember { mutableStateOf(false) }
    var isGpuRenderingEnabled by remember { mutableStateOf(false) }

    // Apply the theme for this screen
    SettingsTheme(darkTheme = isDarkTheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "Developer Options",
                style = CustomTypography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Developer Mode Toggle
            SettingToggleOption(
                title = "Developer Mode",
                subtitle = "Enable or disable developer mode",
                isChecked = isDeveloperModeEnabled,
                onCheckedChange = { isDeveloperModeEnabled = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Logging Toggle
            SettingToggleOption(
                title = "Enable Logging",
                subtitle = "Enable or disable logging for developer features",
                isChecked = isLoggingEnabled,
                onCheckedChange = { isLoggingEnabled = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // GPU Rendering Toggle
            SettingToggleOption(
                title = "Enable GPU Rendering",
                subtitle = "Enable or disable GPU rendering for enhanced performance",
                isChecked = isGpuRenderingEnabled,
                onCheckedChange = { isGpuRenderingEnabled = it }
            )
        }
    }
}

@Composable
fun SettingToggleOption(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF4F4F4),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Text Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = CustomTypography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = CustomTypography.bodyLarge.copy(color = Color.Gray),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// Preview for the Settings Screen
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    var isDarkTheme by remember { mutableStateOf(false) } // Theme state for preview
    SettingsTheme(darkTheme = isDarkTheme) {
        val navController = rememberNavController()
        SettingsScreen(
            wifiSsid = "HomeNetwork",
            onToggleTheme = { isDarkTheme = !isDarkTheme },
            navController = navController
        )
    }
}
