# BluetoothApp

## O aplikaciji
Ova aplikacija namjenjena je za mobilne uređaje koje pokreće operacijski sustav Android.
Aplikacija se može ponašati na dva načina. U prvom načinu rada("Centralni") aplikacija zahtjeva određene 
usluge od drugih mobilnih uređaja koji su neposredno u blizinu te ukoliko je potrebno prenosi
veću količinu podataka(programski je određeno da to bude izabrana slika iz galerije uređaja). U drugom načinu rada("Periferni") 
ponaša kao uređaj koji može odgovoriti na upite "centalnih" uređaja o tome da li može pružiti traženu uslugu i po 
potrebi primati veću količinu podataka.

## Upute o korištenju
Pri pokretanju aplikacije otvara se početni prozor s dva gumba("Scan" i "Transmit") koji služe kao odabir na koji način će
se mobilni uređaj ponašati("centralni" ili "periferni").
Klikom na gumb "Scan" uređaj prelazi u prvi("centralni") način rada u kojim skenira, tj detektira ostale "periferne" uređaje 
u blizini i po potrebi šalje upite za određene usluge koje uređaj zahtjeva. Na prozu se nalaze 3 gumba ("START SCAN", 
"SHOW SENDING FILE SIZE", "CONFIRM TEXT ENTRY") i prostor za unos teksta. Odabirom gumba "START SCAN" pokreće se skeniranje,tj
detekcija "perifernih" uređaja u blizini kojima se može poslat upit za traženom uslugom. Uređaj detektira beacone koje drugi
"periferni" uređaju emitiraju. Ispor prostora za unos tekst pojavljuju se podaci o detektiranim beaconima. Za svaki beacon može se
vidjeti:
* Distance - udaljenost od uređaja koji emitira beacon(uglavnom netočna i treba je zanemariti)
* Rssi - predstavlja snagu primljenog signala
* Uuid - identifikator od 36 znakova, od kojih zadnjih 12 predstavljaju mac adresu uređaja koji emitira beacone)
* Minor i Major - ostala dva identifikatora, broj u rasponu od 0 do 65535 
* Name - ime uređaja koji emitira(koji je null u slučaju da beacone emitiraju mobilni uređaji)
* Address - mac adresa uređaja koji emitira beacon(u slučaju da beacon emitira mobilni uređaj ne predstavlja pravu adresu nego      slučajno generiranu).
* DataFields - dodatno polje koje se emitira, amože sadržavati broj u rasponu od 0 do 255      
* Gumb "Send request via bluetoot" - klikom na gumb salje se upit za određenu uslugu uređaju koji emitira taj kliknuti beacon

Prostor za upis teksta sluzi za upis usluge koja se zahtjeva. Usluga može biti bilo koji niz znakova a potvrđuje se klikom na 
gumb "CONFIRM TEXT ENTRY", nakon čega se u prosotu između gumba i prostota za unos teksta pojavljuje upisana usluga, to je potvrda
da je usluga pravilno registrirana u alikaciji. Kada se klikne na gumb "Send request bia bluetooth"(na određenom beaconu) upravo
se ta usliga zahtjeva od uređaja koji je emitirao taj određeni beacon. Nakon nekog vremena potrebnog za obradu pokazuje se 
potvrdan ili negativan odgovor u obliku skočne poruke(oblik poruke je ime uređaja i mac adresa uređaja koji je poslao odgovor i 
"OK" ukoliko je odgovor potvrdan(uređaj ima traženu uslugu) ili "FAIL" u suprotnom). Usluga "1" je zadana kao defoultna usluga i predstavlja uslugu koja zahtjeva prijenos određene veće količine podataka. Ako se postavi "1" kao tražena usluga te ako "periferni"
uređaj potvrdono odgovori automatski se otvara prikaz memorije, tj datoteka uređaja odakle se treba izabrati bilo koja slika
iz galerije uređaja koja se želi poslati "perifernom" uređaju. Programski je namješteno da se mogu slati samo slike iz galerije, stoga
će se izbaciti greška ako se klikne na bilo koju drugu vrstu datoteke. Greška će vratiti apolikaciju na početni prozor. Klikom na 
sliku, datoteka (slika) će se poćeti prenositi na periferni uređaj. Ako je sve dobro napravljeno u prostoru za tekst(između gumbova
i prostora za upis teksta) pojavit ce se informacija o veličini datoteke(slike) koja se šalje. Klikom na gumb "CONFIRM TEXT ENTRY" 
ponovno se prikazuje usluga koja se trenutno traži, a klikom na srednji gumb "SHOW SENDING FILE SIZE" prikazuje se veličina 
datoteke(slike) koja se zadnja slala ili se trenutno šalje.

Ako bi na početnom prozru aplikacije kliknuli na gumb "TARNSMIT" uređaj prelazi u drugi("periferni") način rada u kojem emitira
beacone i ako može, odnosmo ako može pružizti tu usligu prima veču količinu podataka. Na prozoru se nalaze 3 gumba ("START TRANSMITTING", "SHOW RECEIVED BYTES", "CONFIRM TEXT ENTRY") i prostor za unos teksta. Klikom na gumb "START TRANSMMITNG" uređaj
počinje emitirati beacon s zadanaim osnvnim paramtrima. Kao i kod "centralnog" dijela prostor za unos teksta služi za definiranje
koje sve usluge može pružiti uređaj, usluge se odvajaju zarezom, npr unosom "1,2,3,abc" definira se da urešaj sadržava usluge
1, 2, 3 i abc. Klikom na "CONFIRM TEXT ENTRY" potvrđuje se unos te se dostupne usluge pojavljuju u prostoru između gumbova i prostora
za unos teksta. Svaki put kada do uređaja dođe upit o dostupnosti određene usluge izbacit će se poruka o podacima(ime i
adresa) uređaja koji je poslao upit za uslugom , te ime usluge koju traži. Uređaj automatski obrađuje zahtjev i vraća odgovor "centralnom" uređaju koji je i poslao upit. Ukoliko je tražena usluga "1", a namješteno je da uređaj posjeduju tu uslugu, na uređaj
će se poćeti prenositi podaci koji se salju sa "centralnog" uređaja. Kako bi se vidjelo koliko se podataka prebacilo u određenom 
trenutku klikom na gumb "SHOW RECEIVED BYTES" prikazat ce se trenutno primljeni baytovi u trenutku klika. Kada se prenesu svi podaci
izbacit ce se skočna poruka o ukupnom broju primljenih bajtova podataka što ujedno označava prestanak transakcije. U svakom trenutku 
klikom na "CONFIRM TEXT ENTRY" može se vidjeti trenutno dostupne upisane usluge koje uređaj pruža, a kikom na "SHOW RECEIVED BYTES" 
trenutni broj primljenih bitova, ako se podaci prenose ili broj primljenih bitova od zadnjeg prijenosa ako su se podaci prenjeli.

## Napomene
Pri pokretanju aplikacije ukoliko bluetooth nije uključen aplikacij će zatražiti da se ukljući, uvijet da i aplikacija radila je
da je bluetooth uključen. Nadalje ukoliko se aplikacija pokreće na Android 6.0 verziji ili višoj ulaskom u "centralni" način rada
tražit će se odobrenje da aplikacija zna lokaciju uređaja i da smije pristupiti pohranjenim podacima na uređaju. Lokacijska dozvola
potrebna je za skeniranje, bez pristanka na nju skeniranje nece biti moguće, a dozvola za čitanje unutarnjih podataka potrebna je kako bi se mogli slati podaci sa mobitela. Ove dozvole tražit će se samo jednom kako su odobrene, a ako nisu svaki put kada se pokrece ovaj način rade sve dok se ne odobre. Za uređaje koji imaju manju verziju od Android 6.0 ove dozvole će se podrazumjevat i neće biti tražene.
Nadalje ispravan rad aplikacije u "perifernom" načinu, odnosno načinu koji emitira beacone moguć je jedino ako uređaj koristi Android 5.0 verziju ili višu jer je tek od te verzije uvedeno da mobilni uređaji mogu emitirati beacone. Dakele ako se koristi uređaj sa starijom verzijom softwera aplikacija se neće u potpunosti moći izvoditi. 

## Literatura
### Android Beacon library
https://altbeacon.github.io/android-beacon-library/
https://altbeacon.github.io/android-beacon-library/javadoc/org/altbeacon/beacon/package-summary.html
### Bluetooth Classic
https://developer.android.com/guide/topics/connectivity/bluetooth.html
### Bluetooth low energy
https://developer.android.com/guide/topics/connectivity/bluetooth-le.html
https://learn.adafruit.com/introduction-to-bluetooth-low-energy/gatt
http://toastdroid.com/2014/09/22/android-bluetooth-low-energy-tutorial/
https://documentation.meraki.com/MR/Bluetooth/Bluetooth_Low_Energy_(BLE)

