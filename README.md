# BluetoothApp

## O aplikaciji
Ova aplikacija namijenjena je za mobilne uređaje koje pokreće operacijski sustav Android.
Aplikacija se može ponašati na dva načina. U prvom načinu rada("centralni") aplikacija zahtjeva određene 
usluge od drugih mobilnih uređaja koji se nalaze neposredno u blizini te ako je potrebno prenosi
veću količinu podataka(programski je određeno da to bude izabrana slika iz galerije uređaja). U drugom načinu rada("periferni") 
ponaša se kao uređaj koji može odgovoriti na upite "centralnih" uređaja o tome da li može pružiti traženu uslugu i po 
potrebi primiti veću količinu podataka.

## Upute o korištenju
Pri pokretanju aplikacije otvara se početni prozor s dva gumba("Scan" i "Transmit") koji služe kao odabir na koji će se način mobilni uređaj ponašati("centralni" ili "periferni").
Klikom na gumb "Scan" uređaj prelazi u prvi("centralni") način rada u kojim skenira, tj detektira ostale "periferne" uređaje 
u blizini i po potrebi šalje upite za određene usluge koje uređaj zahtjeva. Na prozoru se nalaze 3 gumba ("START SCAN", 
"SHOW SENDING FILE SIZE", "CONFIRM TEXT ENTRY") i prostor za unos teksta. Odabirom gumba "START SCAN" pokreće se skeniranje,tj
detekcija "perifernih" uređaja u blizini kojima se može poslati upit za traženom uslugom. Uređaj detektira beacone koje drugi
"periferni" uređaju emitiraju. Ispod prostora za unos tekst pojavljuju se podaci o detektiranim beaconima. Za svaki beacon može se
vidjeti:
* Distance - udaljenost od uređaja koji emitira beacon(uglavnom netočna i treba je zanemariti)
* Rssi -  snaga primljenog signala
* Uuid - identifikator od 36 znakova, od kojih zadnjih 12 predstavljaju mac adresu uređaja koji emitira beacon
* Minor i Major - ostala dva identifikatora, broj u rasponu od 0 do 65535 
* Name - ime uređaja koji emitira(null u slučaju da beacon emitira mobilni uređaj)
* Address - mac adresa uređaja koji emitira beacon(u slučaju da beacon emitira mobilni uređaj ne predstavlja pravu adresu nego slučajno generiranu)
* DataFields - dodatno polje, može sadržavati broj u rasponu od 0 do 255      
* Gumb "Send request via Bluetooth" - klikom na gumb šalje se upit za određenu uslugu uređaju koji emitira taj kliknuti beacon.

Prostor za upis teksta služi za upis usluge koja će se tražiti od drugih uređaja. Usluga može biti bilo koji niz znakova. a potvrđuje se klikom na gumb "CONFIRM TEXT ENTRY" nakon čega se u prostor između gumba i prostora za unos teksta pojavljuje upisana usluga što je potvrda pravilnog unosa. Kada se klikne na gumb "Send request via Bluetooth"(na određenom beaconu) upravo se ta usluga zahtjeva od uređaja koji je emitirao određeni beacon. Nakon nekog vremena potrebnog za obradu pokazuje se pozitivan ili negativan odgovor u obliku poruke koja se sastoji od imena i mac adresa uređaja koji je poslao odgovor te odgovora "OK" ukoliko uređaj može pružiti uslugu, odnosno "FAIL" u suprotnom. Usluga "1" je programski zadana kao ona koja zahtjeva prijenos veće količine podataka. Ako se postavi "1" kao tražena usluga te ako "periferni"uređaj potvrdno odgovori automatski se otvara prikaz datoteka uređaja odakle se treba izabrati bilo koja slika iz galerije uređaja koja se želi poslati "perifernom" uređaju. Programski je namješteno da se mogu slati samo slike iz galerije, stoga će se izbaciti greška ako se klikne na bilo koju drugu vrstu datoteke. Greška će vratiti aplikaciju na početni prozor. Klikom na sliku, datoteka će se početi slati na periferni uređaj. Ako je sve dobro napravljeno u prostoru za tekst(između gumbova
i prostora za upis teksta) pojaviti ce se informacija o veličini datoteke(slike) koja se šalje. Klikom na gumb "CONFIRM TEXT ENTRY" 
ponovno se prikazuje usluga koja se trenutno traži, a klikom na srednji gumb "SHOW SENDING FILE SIZE" prikazuje se veličina 
datoteke(slike) koja se zadnja slala ili se trenutno šalje.

Ako bi na početnom prozru aplikacije kliknuli na gumb "TRANSMIT" uređaj prelazi u drugi("periferni") način rada u kojem emitira
beacone, te ako su tako paramenti namješteni može primati podatke od "centralnog" uređaja. Na prozoru se nalaze 3 gumba ("START TRANSMITTING", "SHOW RECEIVED BYTES", "CONFIRM TEXT ENTRY") i prostor za unos teksta. Klikom na gumb "START TRANSMITTING" uređaj počinje emitirati beacon sa zadanim osnovnim parametrima. Kao i kod "centralnog" dijela prostor za unos teksta služi za definiranje koje sve usluge može pružiti uređaj,a usluge se odvajaju zarezom, npr unosom "1,2,3,abc". Klikom na "CONFIRM TEXT ENTRY" potvrđuje se unos te se dostupne usluge pojavljuju u prostoru između gumbova i prostora za unos teksta. Svaki put kada do uređaja dođe upit o dostupnosti određene usluge izbacit će se poruka o podacima(ime i adresa) uređaja koji je poslao upit za uslugom , te naziv usluge koju traži. Uređaj automatski obrađuje zahtjev i vraća odgovor "centralnom" uređaju koji je i poslao upit. Ako je tražena usluga "1", a zadano je da uređaj posjeduju tu uslugu, na uređaj će se početi prenositi podaci koji se šalju sa "centralnog" uređaja. Kako bi se vidjelo koliko se podataka prebacilo u određenom trenutku klikom na gumb "SHOW RECEIVED BYTES" prikazati ce se trenutno primljeni bajtovi u trenutku klika. Kada se prenesu svi podaci izbaciti ce se poruka o ukupnom broju primljenih bajtova podataka što ujedno označava prestanak transakcije. U svakom trenutku klikom na "CONFIRM TEXT ENTRY" može se vidjeti trenutno dostupne upisane usluge koje uređaj pruža, a kikom na "SHOW RECEIVED BYTES" trenutni broj primljenih bajtova, ako se podaci prenose ili broj primljenih bitova od zadnjeg prijenosa ako su se podaci prenijeli.

## Napomene
Ako bluetooth nije uključen pri pokretanju aplikacije zatražit će se da se uključi. Uvijte da bi aplikacija radila je
da je bluetooth uključen, u suprotnom aplikacija neće reagirati. Nadalje ako se aplikacija pokreće na Android 6.0 ili novijoj verziji ulaskom u "centralni" način rada tražit će se odobrenje da aplikacija zna lokaciju uređaja i da smije pristupiti pohranjenim podacima na uređaju. Lokacijska dozvola potrebna je za skeniranje, bez pristanka skeniranje neće biti moguće, a dozvola za čitanje podataka s uređaja potrebna je kako bi se mogli slati podaci s mobitela. Ove dozvole tražit će se samo jednom kako su odobrene, a ako nisu svaki put kada se pokreće ovaj način rada sve dok se ne odobre. Za uređaje koji imaju stariju verziju od Android 6.0 ove dozvole će se podrazumijevat i neće biti tražene. Nadalje ispravan rad aplikacije u "perifernom" načinu, odnosno načinu koji emitira beacone moguć je jedino ako uređaj koristi Android 5.0 verziju ili noviju jer je tek od te verzije uvedeno da mobilni uređaji mogu emitirati beacone. Dakle ako se koristi uređaj sa starijom verzijom softwera aplikacija se neće u potpunosti moći izvoditi. 

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

