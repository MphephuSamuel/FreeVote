package com.example.freevote.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.freevote.R
import com.example.freevote.viewmodel.MainViewModel


val AbhayaLibreExtraBold = FontFamily(
    Font(R.font.abhaya_libre_extrabold)
)

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun VotePage(modifier: Modifier, navController: NavController, viewModel: MainViewModel) {
    // Define the custom font
    val Rubikmoonroocks = FontFamily(
        Font(R.font.rubik_moonrocks)
    )
    fun generateWards(count: Int): List<Ward> {
        return (1..count).map { Ward("Ward $it") }
    }
    val provinces = listOf(
        Province(
            "Eastern Cape",
            districts = listOf(
                District("Alfred Nzo District", locals = listOf(
                    Local("Matatiele Local", wards = generateWards(27)),
                    Local("Ntabankulu Local", wards = generateWards(19)),
                    Local("Umzimvubu Local", wards = generateWards(15)),
                    Local("Winnie Madikizela-Mandela Local", wards = generateWards(10))
                )),
                District("Amathole District", locals = listOf(
                    Local("Amahlathi Local", wards = generateWards(20)),
                    Local("Great Kei Local", wards = generateWards(10)),
                    Local("Mbhashe Local", wards = generateWards(15)),
                    Local("Mnquma Local", wards = generateWards(18)),
                    Local("Ngqushwa Local", wards = generateWards(12)),
                    Local("Raymond Mhlaba Local", wards = generateWards(14))
                )),
                District("Chris Hani District", locals = listOf(
                    Local("Dr AB Xuma Local", wards = generateWards(10)),
                    Local("Emalahleni Local", wards = generateWards(12)),
                    Local("Enoch Mgijima Local", wards = generateWards(20)),
                    Local("Intsika Yethu Local", wards = generateWards(15)),
                    Local("Inxuba Yethemba Local", wards = generateWards(10)),
                    Local("Sakhisizwe Local", wards = generateWards(8))
                )),
                District("Joe Gqabi District", locals = listOf(
                    Local("Elundini Local", wards = generateWards(12)),
                    Local("Senqu Local", wards = generateWards(10)),
                    Local("Walter Sisulu Local", wards = generateWards(8))
                )),
                District("OR Tambo District", locals = listOf(
                    Local("Ingquza Hill Local", wards = generateWards(15)),
                    Local("King Sabata Dalindyebo Local", wards = generateWards(20)),
                    Local("Mhlontlo Local", wards = generateWards(10)),
                    Local("Nyandeni Local", wards = generateWards(12)),
                    Local("Port St Johns Local", wards = generateWards(8))
                )),
                District("Sarah Baartman District", locals = listOf(
                    Local("Blue Crane Route Local", wards = generateWards(10)),
                    Local("Dr Beyers Naudé Local", wards = generateWards(12)),
                    Local("Kouga Local", wards = generateWards(15)),
                    Local("Koukamma Local", wards = generateWards(8)),
                    Local("Makana Local", wards = generateWards(10)),
                    Local("Ndlambe Local", wards = generateWards(8)),
                    Local("Sundays River Valley Local", wards = generateWards(10))
                ))
            ),
            metros = listOf(
                Metro("Buffalo Ciy Metropolitan", wards = generateWards(20)),
                Metro("Nelson Mandela Bay Metropolitan", wards = generateWards(20))
            )
        ),
        Province(
            "Free State",
            districts = listOf(
                District("Fezile Dabi District", locals = listOf(
                    Local("Mafube Local", wards = generateWards(15)),
                    Local("Metsimaholo Local", wards = generateWards(21)),
                    Local("Moqhaka Local", wards = generateWards(27)),
                    Local("Ngwathe Local", wards = generateWards(19))
                )),
                District("Lejweleputswa District", locals = listOf(
                    Local("Masilonyana Local", wards = generateWards(15)),
                    Local("Matjhabeng Local", wards = generateWards(36)),
                    Local("Nala Local", wards = generateWards(14)),
                    Local("Tokologo Local", wards = generateWards(8)),
                    Local("Tswelopele Local", wards = generateWards(10))
                )),
                District("Thabo Mofutsanyana District", locals = listOf(
                    Local("Dihlabeng Local", wards = generateWards(19)),
                    Local("Maluti-A-Phofung Local", wards = generateWards(35)),
                    Local("Mantsopa Local", wards = generateWards(9)),
                    Local("Nketoana Local", wards = generateWards(11)),
                    Local("Phumelela Local", wards = generateWards(8)),
                    Local("Setsoto Local", wards = generateWards(16))
                )),
                District("Xhariep District", locals = listOf(
                    Local("Kopanong Local", wards = generateWards(15)),
                    Local("Letsemeng Local", wards = generateWards(10)),
                    Local("Mohokare Local", wards = generateWards(8))
                ))
            ),
            metros = listOf(
                Metro("Mangaung Metropolitan", wards = generateWards(50))
            )
        ),
        Province(
            "Gauteng",
            districts = listOf(
                District("Sedibeng District", locals = listOf(
                    Local("Emfuleni Local", wards = generateWards(45)),
                    Local("Lesedi Local", wards = generateWards(15)),
                    Local("Midvaal Local", wards = generateWards(10))
                )),
                District("West Rand District", locals = listOf(
                    Local("Merafong City Local", wards = generateWards(28)),
                    Local("Mogale City Local", wards = generateWards(39)),
                    Local("Rand West City Local", wards = generateWards(22))
                ))
            ),
            metros = listOf(
                Metro("City of Ekurhuleni Metropolitan", wards = generateWards(112)),
                Metro("City of Johannesburg Metropolitan", wards = generateWards(135)),
                Metro("City of Tshwane Metropolitan", wards = generateWards(107))
            )
        ),
        Province(
            "KwaZulu-Natal",
            districts = listOf(
                District("Amajuba District", locals = listOf(
                    Local("Dannhauser Local", wards = generateWards(11)),
                    Local("eMadlangeni Local", wards = generateWards(7)),
                    Local("Newcastle Local", wards = generateWards(34))
                )),
                District("Harry Gwala District", locals = listOf(
                    Local("Dr Nkosazana Dlamini Zuma Local", wards = generateWards(13)),
                    Local("Greater Kokstad Local", wards = generateWards(8)),
                    Local("Ubuhlebezwe Local", wards = generateWards(12)),
                    Local("Umzimkhulu Local", wards = generateWards(20))
                )),
                District("iLembe District", locals = listOf(
                    Local("KwaDukuza Local", wards = generateWards(29)),
                    Local("Mandeni Local", wards = generateWards(17)),
                    Local("Maphumulo Local", wards = generateWards(11)),
                    Local("Ndwedwe Local", wards = generateWards(19))
                )),
                District("King Cetshwayo District", locals = listOf(
                    Local("City of uMhlathuze Local", wards = generateWards(34)),
                    Local("Mthonjaneni Local", wards = generateWards(12)),
                    Local("Nkandla Local", wards = generateWards(13)),
                    Local("uMfolozi Local", wards = generateWards(11)),
                    Local("uMlalazi Local", wards = generateWards(27))
                )),
                District("Ugu District", locals = listOf(
                    Local("Ray Nkonyeni Local", wards = generateWards(36)),
                    Local("Umdoni Local", wards = generateWards(10)),
                    Local("Umuziwabantu Local", wards = generateWards(9)),
                    Local("Umzumbe Local", wards = generateWards(19))
                )),
                District("uMgungundlovu District", locals = listOf(
                    Local("Impendle Local", wards = generateWards(4)),
                    Local("Mkhambathini Local", wards = generateWards(7)),
                    Local("Mpofana Local", wards = generateWards(4)),
                    Local("Msunduzi Local", wards = generateWards(39)),
                    Local("Richmond Local", wards = generateWards(7)),
                    Local("uMngeni Local", wards = generateWards(12)),
                    Local("uMshwathi Local", wards = generateWards(11))
                )),
                District("uMkhanyakude District", locals = listOf(
                    Local("Big 5 Hlabisa Local", wards = generateWards(8)),
                    Local("Inkosi uMtubatuba Local", wards = generateWards(10)),
                    Local("Jozini Local", wards = generateWards(20)),
                    Local("uMhlabuyalingana Local", wards = generateWards(15))
                )),
                District("uMzinyathi District", locals = listOf(
                    Local("Endumeni Local", wards = generateWards(7)),
                    Local("Nquthu Local", wards = generateWards(17)),
                    Local("uMsinga Local", wards = generateWards(19)),
                    Local("Umvoti Local", wards = generateWards(13))
                )),
                District("uThukela District", locals = listOf(
                    Local("Alfred Duma Local", wards = generateWards(37)),
                    Local("Inkosi Langalibalele Local", wards = generateWards(22)),
                    Local("Okhahlamba Local", wards = generateWards(14))
                )),
                District("Zululand District", locals = listOf(
                    Local("AbaQulusi Local", wards = generateWards(24)),
                    Local("eDumbe Local", wards = generateWards(8)),
                    Local("Nongoma Local", wards = generateWards(14)),
                    Local("Ulundi Local", wards = generateWards(24)),
                    Local("uPhongolo Local", wards = generateWards(13))
                ))
            ),
            metros = listOf(
                Metro("eThekwini Metropolitan", wards = generateWards(110))
            )
        ),
        Province(
            "Limpopo",
            districts = listOf(
                District("Capricorn District", locals = listOf(
                    Local("Blouberg Local", wards = generateWards(18)),
                    Local("Lepelle-Nkumpi Local", wards = generateWards(27)),
                    Local("Molemole Local", wards = generateWards(14)),
                    Local("Polokwane Local", wards = generateWards(45))
                )),
                District("Mopani District", locals = listOf(
                    Local("Ba-Phalaborwa Local", wards = generateWards(17)),
                    Local("Greater Giyani Local", wards = generateWards(30)),
                    Local("Greater Letaba Local", wards = generateWards(25)),
                    Local("Greater Tzaneen Local", wards = generateWards(35)),
                    Local("Maruleng Local", wards = generateWards(14))
                )),
                District("Sekhukhune District", locals = listOf(
                    Local("Elias Motsoaledi Local", wards = generateWards(30)),
                    Local("Ephraim Mogale Local", wards = generateWards(16)),
                    Local("Fetakgomo Tubatse Local", wards = generateWards(39)),
                    Local("Makhuduthamaga Local", wards = generateWards(32))
                )),
                District("Vhembe District", locals = listOf(
                    Local("Collins Chabane Local", wards = generateWards(36)),
                    Local("Makhado Local", wards = generateWards(38)),
                    Local("Musina Local", wards = generateWards(12)),
                    Local("Thulamela Local", wards = generateWards(41))
                )),
                District("Waterberg District", locals = listOf(
                    Local("Bela-Bela Local", wards = generateWards(14)),
                    Local("Lephalale Local", wards = generateWards(22)),
                    Local("Modimolle-Mookgophong Local", wards = generateWards(14)),
                    Local("Mogalakwena Local", wards = generateWards(32)),
                    Local("Thabazimbi Local", wards = generateWards(12))
                ))
            ),
            metros = listOf()
        ),
        Province(
            "Mpumalanga",
            districts = listOf(
                District("Ehlanzeni District", locals = listOf(
                    Local("Bushbuckridge Local", wards = generateWards(37)),
                    Local("City of Mbombela Local", wards = generateWards(45)),
                    Local("Nkomazi Local", wards = generateWards(33)),
                    Local("Thaba Chweu Local", wards = generateWards(15))
                )),
                District("Gert Sibande District", locals = listOf(
                    Local("Chief Albert Luthuli Local", wards = generateWards(25)),
                    Local("Dipaleseng Local", wards = generateWards(8)),
                    Local("Dr Pixley Ka Isaka Seme Local", wards = generateWards(13)),
                    Local("Govan Mbeki Local", wards = generateWards(35)),
                    Local("Lekwa Local", wards = generateWards(15)),
                    Local("Mkhondo Local", wards = generateWards(19)),
                    Local("Msukaligwa Local", wards = generateWards(19))
                )),
                District("Nkangala District", locals = listOf(
                    Local("Dr JS Moroka Local", wards = generateWards(32)),
                    Local("Emakhazeni Local", wards = generateWards(8)),
                    Local("Emalahleni Local", wards = generateWards(34)),
                    Local("Steve Tshwete Local", wards = generateWards(29)),
                    Local("Thembisile Hani Local", wards = generateWards(32)),
                    Local("Victor Khanye Local", wards = generateWards(9))
                ))
            ),
            metros = listOf()
        ),
        Province(
            "Northern Cape",
            districts = listOf(
                District("Frances Baard District", locals = listOf(
                    Local("Dikgatlong Local", wards = generateWards(11)),
                    Local("Magareng Local", wards = generateWards(6)),
                    Local("Phokwane Local", wards = generateWards(14)),
                    Local("Sol Plaatje Local", wards = generateWards(33))
                )),
                District("John Taolo Gaetsewe District", locals = listOf(
                    Local("Ga-Segonyana Local", wards = generateWards(15)),
                    Local("Gamagara Local", wards = generateWards(9)),
                    Local("Joe Morolong Local", wards = generateWards(17))
                )),
                District("Namakwa District", locals = listOf(
                    Local("Hantam Local", wards = generateWards(6)),
                    Local("Kamiesberg Local", wards = generateWards(4)),
                    Local("Karoo Hoogland Local", wards = generateWards(4)),
                    Local("Khai-Ma Local", wards = generateWards(4)),
                    Local("Nama Khoi Local", wards = generateWards(8)),
                    Local("Richtersveld Local", wards = generateWards(4))
                )),
                District("Pixley Ka Seme District", locals = listOf(
                    Local("Emthanjeni Local", wards = generateWards(8)),
                    Local("Kareeberg Local", wards = generateWards(4)),
                    Local("Renosterberg Local", wards = generateWards(4)),
                    Local("Siyancuma Local", wards = generateWards(8)),
                    Local("Siyathemba Local", wards = generateWards(6)),
                    Local("Thembelihle Local", wards = generateWards(4)),
                    Local("Ubuntu Local", wards = generateWards(4)),
                    Local("Umsobomvu Local", wards = generateWards(6))
                )),
                District("ZF Mgcawu District", locals = listOf(
                    Local("!Kheis Local", wards = generateWards(4)),
                    Local("Dawid Kruiper Local", wards = generateWards(14)),
                    Local("Kai !Garib Local", wards = generateWards(8)),
                    Local("Kgatelopele Local", wards = generateWards(4)),
                    Local("Tsantsabane Local", wards = generateWards(6))
                ))
            ),
            metros = listOf()
        ),
        Province(
            "North West",
            districts = listOf(
                District("Bojanala Platinum District", locals = listOf(
                    Local("Kgetlengrivier Local", wards = generateWards(10)),
                    Local("Madibeng Local", wards = generateWards(41)),
                    Local("Moretele Local", wards = generateWards(26)),
                    Local("Moses Kotane Local", wards = generateWards(34)),
                     Local("Rustenburg Local", wards = generateWards(45))
                )),
                District("Dr Kenneth Kaunda District", locals = listOf(
                    Local("City of Matlosana Local", wards = generateWards(39)),
                    Local("JB Marks Local", wards = generateWards(31)),
                    Local("Maquassi Hills Local", wards = generateWards(10))
                )),
                District("Dr Ruth Segomotsi Mompati District", locals = listOf(
                    Local("Greater Taung Local", wards = generateWards(32)),
                    Local("Kagisano-Molopo Local", wards = generateWards(15)),
                    Local("Lekwa-Teemane Local", wards = generateWards(8)),
                    Local("Mamusa Local", wards = generateWards(7)),
                    Local("Naledi Local", wards = generateWards(8))
                )),
                District("Ngaka Modiri Molema District", locals = listOf(
                    Local("Ditsobotla Local", wards = generateWards(19)),
                    Local("Mahikeng Local", wards = generateWards(35)),
                    Local("Ramotshere Moiloa Local", wards = generateWards(17)),
                    Local("Ratlou Local", wards = generateWards(14)),
                    Local("Tswaing Local", wards = generateWards(15))
                ))
            ),
            metros = listOf()
        ),
        Province(
            "Western Cape",
            districts = listOf(
                District("Cape Winelands District", locals = listOf(
                    Local("Breede Valley Local", wards = generateWards(23)),
                    Local("Drakenstein Local", wards = generateWards(33)),
                    Local("Langeberg Local", wards = generateWards(14)),
                    Local("Stellenbosch Local", wards = generateWards(22)),
                    Local("Witzenberg Local", wards = generateWards(12))
                )),
                District("Central Karoo District", locals = listOf(
                    Local("Beaufort West Local", wards = generateWards(7)),
                    Local("Laingsburg Local", wards = generateWards(4)),
                    Local("Prince Albert Local", wards = generateWards(4))
                )),
                District("Garden Route District", locals = listOf(
                    Local("Bitou Local", wards = generateWards(7)),
                    Local("George Local", wards = generateWards(27)),
                    Local("Hessequa Local", wards = generateWards(8)),
                    Local("Kannaland Local", wards = generateWards(4)),
                    Local("Knysna Local", wards = generateWards(11)),
                    Local("Mossel Bay Local", wards = generateWards(14)),
                    Local("Oudtshoorn Local", wards = generateWards(13))
                )),
                District("Overberg District", locals = listOf(
                    Local("Cape Agulhas Local", wards = generateWards(6)),
                    Local("Overstrand Local", wards = generateWards(13)),
                    Local("Swellendam Local", wards = generateWards(6)),
                    Local("Theewaterskloof Local", wards = generateWards(13))
                )),
                District("West Coast District", locals = listOf(
                    Local("Bergrivier Local", wards = generateWards(8)),
                    Local("Cederberg Local", wards = generateWards(6)),
                    Local("Matzikama Local", wards = generateWards(8)),
                    Local("Saldanha Bay Local", wards = generateWards(14)),
                    Local("Swartland Local", wards = generateWards(12))
                ))
            ),
            metros = listOf(
                Metro("City of Cape Town Metropolitan", wards = generateWards(116))
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Text header
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append("FREE")
                }
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append("vote")
                }
                withStyle(style = SpanStyle(color = Color(0xFF006400))) {
                    append("!")
                }
            },
            fontFamily = Rubikmoonroocks,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 48.sp,
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(4f, 4f),
                    blurRadius = 8f
                )
            ),
            modifier = Modifier.padding(16.dp)
        )

        // Location selection section
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, shape = RectangleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Location",
                    fontFamily = AbhayaLibreExtraBold,
                    fontSize = 32.sp
                )
                // Call LocationSelection here with provinces list
                LocationDropdown(provinces)
            }
        }

        // Spacer between sections
        Spacer(
            modifier = Modifier.fillMaxWidth().height(16.dp)
        )

        // Vote section (assuming you will implement VoteDropdownMenu similarly)
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, shape = RectangleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Vote",
                    fontFamily = AbhayaLibreExtraBold,
                    fontSize = 32.sp
                )
                // Assuming you have a similar VoteDropdownMenu composable
                VoteDropdownMenu()
            }
        }
    }
}
data class Ward(val name: String)
data class Local(val name: String, val wards: List<Ward>)
data class District(val name: String, val locals: List<Local>)
data class Metro(val name: String, val wards: List<Ward>)
data class Province(val name: String, val districts: List<District>, val metros: List<Metro>)

@Composable
fun LocationDropdown(provinces: List<Province>) {
    // State variables for managing dropdown visibility and selected items
    var expandedProvince by remember { mutableStateOf(false) }
    var expandedDistrictMetro by remember { mutableStateOf(false) }
    var expandedLocal by remember { mutableStateOf(false) }
    var expandedWard by remember { mutableStateOf(false) }

    var selectedProvince by remember { mutableStateOf<Province?>(null) }
    var selectedDistrict by remember { mutableStateOf<District?>(null) }
    var selectedMetro by remember { mutableStateOf<Metro?>(null) }
    var selectedLocal by remember { mutableStateOf<Local?>(null) }
    var selectedWard by remember { mutableStateOf<Ward?>(null) }

    var locationConfirmation by remember { mutableStateOf(false) }
    var showFloatingButton by remember { mutableStateOf(false) }
    var showFloatingButtonDistrict by remember { mutableStateOf(false)}
    var showFloatingButtonMetro by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // Province Dropdown
        Button(
            onClick = { expandedProvince = !expandedProvince },
            modifier = Modifier.fillMaxWidth().border(BorderStroke(1.dp, Color.Black)),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xE2E2E2), contentColor = Color.Black)
        ) {
            Text(text = selectedProvince?.name ?: "Select Province")
        }

        DropdownMenu(
            expanded = expandedProvince,
            onDismissRequest = { expandedProvince = false },
            modifier = Modifier.fillMaxWidth().background(Color.White).border(1.dp, Color.Gray).padding(8.dp)
        ) {
            provinces.forEachIndexed { index, province ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(province.name)
                            RadioButton(
                                selected = selectedProvince == province,
                                onClick = {
                                    selectedProvince = province
                                    selectedDistrict = null
                                    selectedMetro = null
                                    selectedLocal = null
                                    selectedWard = null
                                    showFloatingButton = true
                                }
                            )
                        }
                    },
                    onClick = {
                        selectedProvince = province
                        selectedDistrict = null
                        selectedMetro = null
                        selectedLocal = null
                        selectedWard = null
                        showFloatingButton = true
                    },

                    )
                // Add a divider between items, but not after the last one
                if (index < provinces.size - 1) {
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                }
            }
            // Animated button that slides from the bottom when province is selected
            AnimatedVisibility(
                visible = showFloatingButton,
                enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
            ){
                FloatingActionButton(
                    onClick = {
                        // Handle button click
                        expandedProvince = false
                        showFloatingButton = false
                        expandedDistrictMetro = true
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Confirm Province")
                    }
                }
            }
        }


        // District/Metro Dropdown
        if (selectedProvince != null) {
            Button(
                onClick = { expandedDistrictMetro = !expandedDistrictMetro },
                modifier = Modifier.fillMaxWidth().border(BorderStroke(1.dp, Color.Black)),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xE2E2E2), contentColor = Color.Black)
            ) {
                Text(text = selectedDistrict?.name ?: selectedMetro?.name ?: "Select District/Metro")
            }

            DropdownMenu(
                expanded = expandedDistrictMetro,
                onDismissRequest = { expandedDistrictMetro = false },
                modifier = Modifier.fillMaxWidth().background(Color.White).border(1.dp, Color.Gray).padding(8.dp)
            ) {
                selectedProvince!!.districts.forEachIndexed {index, district ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(district.name)
                                RadioButton(
                                    selected = selectedDistrict == district,
                                    onClick = {
                                        selectedDistrict = district
                                        selectedMetro = null
                                        selectedLocal = null
                                        selectedWard = null
                                        showFloatingButtonDistrict = true
                                        showFloatingButtonMetro = false
                                    }
                                )
                            }
                        },
                        onClick = {
                            selectedDistrict = district
                            selectedMetro = null
                            selectedLocal = null
                            selectedWard = null
                            showFloatingButtonDistrict = true
                            showFloatingButtonMetro = false
                        }
                    )
                    // Add a divider between items, but not after the last one
                    if (index < selectedProvince!!.districts.size - 1) {
                        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                    }
                }
                // Animated button that slides from the bottom when province is selected
                AnimatedVisibility(
                    visible = showFloatingButtonDistrict,
                    enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                    exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
                ){
                    FloatingActionButton(
                        onClick = {
                            // Handle button click
                            expandedDistrictMetro = false
                            showFloatingButtonDistrict = false
                            expandedLocal = true
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Confirm District")
                        }
                    }
                }
                selectedProvince!!.metros.forEachIndexed {index, metro ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(metro.name)
                                RadioButton(
                                    selected = selectedMetro == metro,
                                    onClick = {
                                        selectedMetro = metro
                                        selectedDistrict = null
                                        selectedLocal = null
                                        selectedWard = null
                                        showFloatingButtonMetro = true
                                        showFloatingButtonDistrict = false
                                    }
                                )
                            }
                        },
                        onClick = {
                            selectedMetro = metro
                            selectedDistrict = null
                            selectedLocal = null
                            selectedWard = null
                            showFloatingButtonMetro = true
                            showFloatingButtonDistrict = false
                        }
                    )
                    // Add a divider between items, but not after the last one
                    if (index < selectedProvince!!.metros.size - 1) {
                        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                    }
                }
                // Animated button that slides from the bottom when province is selected
                AnimatedVisibility(
                    visible = showFloatingButtonMetro,
                    enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                    exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
                ){
                    FloatingActionButton(
                        onClick = {
                            // Handle button click
                            expandedDistrictMetro = false
                            showFloatingButtonMetro = false
                            expandedWard = true
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Confirm Metro")
                        }
                    }
                }
            }

            // Local Dropdown
            if (selectedDistrict != null) {
                Button(
                    onClick = { expandedLocal = !expandedLocal },
                    modifier = Modifier.fillMaxWidth().border(BorderStroke(1.dp, Color.Black)),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xE2E2E2), contentColor = Color.Black)
                ) {
                    Text(text = selectedLocal?.name ?: "Select Local")
                }

                DropdownMenu(
                    expanded = expandedLocal,
                    onDismissRequest = { expandedLocal = false },
                    modifier = Modifier.fillMaxWidth().background(Color.White).border(1.dp, Color.Gray).padding(8.dp)
                ) {
                    selectedDistrict!!.locals.forEachIndexed {index, local ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(local.name)
                                    RadioButton(
                                        selected = selectedLocal == local,
                                        onClick = {
                                            selectedLocal = local
                                            selectedWard = null
                                            showFloatingButton = true
                                        }
                                    )
                                }
                            },
                            onClick = {
                                selectedLocal = local
                                selectedWard = null
                                showFloatingButton = true
                            }
                        )
                        // Add a divider between items, but not after the last one
                        if (index < selectedDistrict!!.locals.size - 1) {
                            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                        }
                    }
                    // Animated button that slides from the bottom when province is selected
                    AnimatedVisibility(
                        visible = showFloatingButton,
                        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
                    ){
                        FloatingActionButton(
                            onClick = {
                                // Handle button click
                                expandedLocal = false
                                showFloatingButton = false
                                expandedWard = true
                            },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(16.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Confirm Local")
                            }
                        }
                    }
                }
            }

            // Ward Dropdown
            if (selectedLocal != null || selectedMetro != null) {
                Button(
                    onClick = { expandedWard = !expandedWard },
                    modifier = Modifier.fillMaxWidth().border(BorderStroke(1.dp, Color.Black)),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xE2E2E2), contentColor = Color.Black)
                ) {
                    Text(text = selectedWard?.name ?: "Select Ward")
                }

                DropdownMenu(
                    expanded = expandedWard,
                    onDismissRequest = { expandedWard = false },
                    modifier = Modifier.fillMaxWidth().background(Color.White).border(1.dp, Color.Gray).padding(8.dp)
                ) {
                    val wards = selectedLocal?.wards ?: selectedMetro?.wards ?: emptyList()
                    wards.forEachIndexed {index, ward ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(ward.name)
                                    RadioButton(
                                        selected = selectedWard == ward,
                                        onClick = {
                                            selectedWard = ward
                                            showFloatingButton = true
                                        }
                                    )
                                }
                            },
                            onClick = {
                                selectedWard = ward
                                showFloatingButton = true
                            }
                        )
                        // Add a divider between items, but not after the last one
                        if (index < wards.size - 1) {
                            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                        }
                    }
                    // Animated button that slides from the bottom when province is selected
                    AnimatedVisibility(
                        visible = showFloatingButton,
                        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight })
                    ){
                        FloatingActionButton(
                            onClick = {
                                // Handle button click
                                expandedWard = false
                                showFloatingButton = false
                                locationConfirmation = true
                            },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(16.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Confirm Ward")
                            }
                        }
                    }
                }
            }
        }

        // location confirmation dialog
        if (locationConfirmation) {
            AlertDialog(
                onDismissRequest = { locationConfirmation = false },
                title = { Text("Confirm Location") },
                text = {
                    Column {
                        Text("Province: ${selectedProvince?.name ?: "None"}")
                        Text("Distric/Metro: ${selectedDistrict?.name ?: selectedMetro?.name ?: "None"}")
                        Text("Local Municipality: ${selectedLocal?.name ?: "None"}")
                        Text("Ward: ${selectedWard?.name ?: "None"}")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            locationConfirmation = false
                            selectedProvince = null
                            selectedDistrict = null
                            selectedMetro = null
                            selectedWard = null
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            locationConfirmation = false
                            selectedProvince = null
                            selectedDistrict = null
                            selectedMetro = null
                            selectedWard = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun VoteDropdownMenu() {
    // State variables for each dropdown
    var expandedNationalVote by remember { mutableStateOf(false) }
    var expandedNational by remember { mutableStateOf(false) }
    var expandedRegional by remember { mutableStateOf(false) }
    var expandedProvincial by remember { mutableStateOf(false) }
    var expandedMunicipalVote by remember { mutableStateOf(false) }
    var expandedWardVote by remember { mutableStateOf(false) }
    var expandedPRVote by remember { mutableStateOf(false) }

    // State variables to hold the selected values
    var selectedNational by remember { mutableStateOf<String?>(null) }
    var selectedRegional by remember { mutableStateOf<String?>(null) }
    var selectedProvincial by remember { mutableStateOf<String?>(null) }
    var selectedWard by remember { mutableStateOf<String?>(null) }
    var selectedPR by remember { mutableStateOf<String?>(null) }

    // State variables to show confirmation dialogs
    var showNationalConfirmation by remember { mutableStateOf(false) }
    var showMunicipalConfirmation by remember { mutableStateOf(false) }

    // Sample data for National, Regional, Provincial, Ward Councillor, and PR Councillor votes
    //val nationalOptions = listOf("Candidate 1", "Candidate 2", "Candidate 3")
    val regionalOptions = listOf("Candidate 1", "Candidate 2", "Candidate 3")
    val provincialOptions = listOf("Candidate 1", "Candidate 2", "Candidate 3")
    val wardOptions = listOf("Ward Candidate 1", "Ward Candidate 2", "Ward Candidate 3")
    val prOptions = listOf("PR Candidate 1", "PR Candidate 2", "PR Candidate 3")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // National Vote Dropdown
        Button(
            onClick = { expandedNationalVote = !expandedNationalVote },
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.Black), shape = RectangleShape),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xE2E2E2), contentColor = Color.Black)
        ) {
            Text(text = "National ")
        }

        DropdownMenu(
            expanded = expandedNationalVote,
            onDismissRequest = { expandedNationalVote = false },
            modifier = Modifier.fillMaxWidth().background(Color.White).border(1.dp, Color.Gray).padding(8.dp)
        ) {
            DropdownMenuItem(
                text = {
                    Column {
                        Text("National", fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(selectedNational ?: "")
                    }
                },
                onClick = {
                    expandedNational = true
                    expandedNationalVote = false // Hide main dropdown
                }
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            DropdownMenuItem(
                text = {
                    Column {
                        Text("Regional", fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(selectedRegional ?: "", )
                    }
                },
                onClick = {
                    expandedRegional = true
                    expandedNationalVote = false // Hide main dropdown
                }
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            DropdownMenuItem(
                text = {
                    Column {
                        Text("Provincial", fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(selectedProvincial ?: "")
                    }
                },
                onClick = {
                    expandedProvincial = true
                    expandedNationalVote = false // Hide main dropdown
                }
            )
        }
        data class PartyOption(
            val name: String,
            val leaderFaceRes: Int,  // Use Int for resource ID
            val abbreviation: String,
            val logoRes: Int         // Use Int for resource ID
        )
        val nationalOptions = listOf(
            PartyOption("Economic Freedom Fighters", R.drawable.malema, "EFF", R.drawable.eff),
            PartyOption("African National Congress", R.drawable.ramaphosa, "ANC", R.drawable.anc),
            PartyOption("uMkhonto Wesizwe", R.drawable.zuma, "MK", R.drawable.mk)
        )

        // National Vote Options
        DropdownMenu(
            expanded = expandedNational,
            onDismissRequest = { expandedNational = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xff04a0df))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xff04a0df), Color.White)
                    )
                )
                .padding(8.dp)
        ) {
            nationalOptions.forEachIndexed {index, option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Leader face image from resources
                            Image(
                                painter = painterResource(option.leaderFaceRes),
                                contentDescription = "Leader Face",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                            )
                            // Party details
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                            ) {
                                Text(text = option.name, fontWeight = FontWeight.Bold)
                                Text(text = option.abbreviation, color = Color.Gray)
                            }
                            // Party logo image from resources
                            Image(
                                painter = painterResource(option.logoRes),
                                contentDescription = "Party Logo",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                            )
                            CrossCheckbox(
                                checked = option.name == selectedNational,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedNational = option.name
                                        expandedNational = false
                                        expandedNationalVote = true
                                    }
                                }
                            )
                        }
                    },
                    onClick = {
                        selectedNational = option.name
                        expandedNational = false
                        expandedNationalVote = true
                    }
                )
                // Add a divider between items, but not after the last one
                if (index < nationalOptions.size - 1) {
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                }
            }
        }

        // Regional Vote Options
        DropdownMenu(
            expanded = expandedRegional,
            onDismissRequest = { expandedRegional = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xfffcae55), Color.White)
                    )
                )
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            regionalOptions.forEachIndexed {index, option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = option, modifier = Modifier.padding(start = 8.dp))
                            CrossCheckbox(
                                checked = option == selectedRegional,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedRegional = option
                                    }
                                }
                            )
                        }
                    },
                    onClick = {
                        selectedRegional = option
                        expandedRegional = false
                        expandedNationalVote = true
                    }
                )
                // Add a divider between items, but not after the last one
                if (index < regionalOptions.size - 1) {
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                }
            }
        }

        // Provincial Vote Options
        DropdownMenu(
            expanded = expandedProvincial,
            onDismissRequest = { expandedProvincial = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xffdd88ab), Color.White)
                    )
                )
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            provincialOptions.forEachIndexed {index, option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = option, modifier = Modifier.padding(start = 8.dp))
                            CrossCheckbox(
                                checked = option == selectedProvincial,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedProvincial = option
                                        showNationalConfirmation = true

                                    }
                                }
                            )
                        }
                    },
                    onClick = {
                        selectedProvincial = option
                        expandedProvincial = false
                        showNationalConfirmation = true
                    }
                )
                // Add a divider between items, but not after the last one
                if (index < provincialOptions.size - 1) {
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Municipality Votes Button
        Button(
            onClick = { expandedMunicipalVote = !expandedMunicipalVote },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    BorderStroke(1.dp, Color.Black),
                    shape = RectangleShape
                ),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xE2E2E2),
                contentColor = Color.Black
            )
        ) {
            Text(text = "Municipality Votes")
        }

        DropdownMenu(
            expanded = expandedMunicipalVote,
            onDismissRequest = { expandedMunicipalVote = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            DropdownMenuItem(
                text = { Text("Ward Councillor", fontWeight = FontWeight.Bold, color = Color.Black) },
                onClick = {
                    expandedWardVote = true
                    expandedMunicipalVote = false // Hide the main dropdown
                }
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            DropdownMenuItem(
                text = { Text("PR Councillor", fontWeight = FontWeight.Bold, color = Color.Black) },
                onClick = {
                    expandedPRVote = true
                    expandedMunicipalVote = false // Hide the main dropdown
                }
            )
        }

        // Ward Councillor Vote Options
        DropdownMenu(
            expanded = expandedWardVote,
            onDismissRequest = { expandedWardVote = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            wardOptions.forEachIndexed {index, option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = option, modifier = Modifier.padding(start = 8.dp))
                            CrossCheckbox(
                                checked = option == selectedWard,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedWard = option
                                    }
                                }
                            )
                        }
                    },
                    onClick = {
                        selectedWard = option
                        expandedWardVote = false
                        expandedMunicipalVote = true
                    }
                )
                // Add a divider between items, but not after the last one
                if (index < wardOptions.size - 1) {
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                }
            }
        }

        // PR Councillor Vote Options
        DropdownMenu(
            expanded = expandedPRVote,
            onDismissRequest = { expandedPRVote = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(1.dp, Color.Gray)
                .padding(8.dp)
        ) {
            prOptions.forEachIndexed {index, option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = option, modifier = Modifier.padding(start = 8.dp))
                            CrossCheckbox(
                                checked = option == selectedPR,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedPR = option
                                    }
                                }
                            )
                        }
                    },
                    onClick = {
                        selectedPR = option
                        expandedPRVote = false
                        showMunicipalConfirmation = true

                    }
                )
                // Add a divider between items, but not after the last one
                if (index < prOptions.size - 1) {
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                }
            }
        }

        // National Vote Confirmation Dialog
        if (showNationalConfirmation) {
            AlertDialog(
                onDismissRequest = { showNationalConfirmation = false },
                title = { Text("Confirm National Votes") },
                text = {
                    Column {
                        Text("National: ${selectedNational ?: "None"}")
                        Text("Regional: ${selectedRegional ?: "None"}")
                        Text("Provincial: ${selectedProvincial ?: "None"}")
                    }
                },
                confirmButton = {
                    val context = LocalContext.current
                    Button(
                        onClick = {
                            showNationalConfirmation = false
                            Toast.makeText(
                                context,
                                "National Votes Submitted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showNationalConfirmation = false
                            selectedNational = null
                            selectedRegional = null
                            selectedProvincial = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Municipal Vote Confirmation Dialog
        if (showMunicipalConfirmation) {
            AlertDialog(
                onDismissRequest = { showMunicipalConfirmation = false },
                title = { Text("Confirm Municipality Votes") },
                text = {
                    Column {
                        Text("Ward Councillor: ${selectedWard ?: "None"}")
                        Text("PR Councillor: ${selectedPR ?: "None"}")
                    }
                },
                confirmButton = {
                    val context = LocalContext.current
                    Button(
                        onClick = {
                            showMunicipalConfirmation = false
                            Toast.makeText(
                                context,
                                "Municipality Votes Submitted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showMunicipalConfirmation = false
                            selectedPR = null
                            selectedWard = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun CrossCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .background(
                color = if (checked) Color.Red else Color.White, // Background color
                shape = RoundedCornerShape(4.dp)
            )
            .border(2.dp, Color.Gray, RoundedCornerShape(4.dp))
            .clickable(onClick = { onCheckedChange?.invoke(!checked) })
    ) {
        if (checked) {
            Text(
                text = "X",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
