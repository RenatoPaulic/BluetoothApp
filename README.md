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
Distance - udaljenost od uređaja koji emitira beacon(uglavnom netočna i treba je zanemariti)
Rssi - predstavlja snagu primljenog signala
Uuid - identifikator od 36 znakova, od kojih zadnjih 12 predstavljaju mac adresu uređaja koji emitira beacone)
Minor i Major - ostala dva identifikatora, broj u rasponu od 0 do 655 
Name - ime uređaja koji emitira(koji je null u slučaju da beacone emitiraju mobilni uređaji)
Address - mac adresa uređaja koji emitira beacon(u slučaju da beacon emitira mobilni uređaj ne predstavlja pravu adresu nego
      slučajno generiranu).
Gumb "Send request via bluetoot" - klikom na gumb salje se upit za određenu uslugu uređaju koji emitira taj kliknuti beacon
Prostor za upis teksta sluzi za upis usluge koja se zahtjeva. Usluga može biti bilo koji niz znakova a potvrđuje se klikom na 
gumb "CONFIRM TEXT ENTRY", nakon čega se u prosotu između gumba i prostota za unos teksta pojavljuje upisana usluga, to je potvrda
da je usluga pravilno registrirana u alikaciji. Kada se klikne na gumb "Send request bia bluetooth"(na određenom beaconu) upravo
se ta usliga zahtjeva od uređaja koji je emitirao taj određeni beacon. Nakon nekog vremena potrebnog za obradu pokazuje se 
potvrdan ili negativan odgovor u obliku skočne poruke(oblik poruke je ime uređaja i mac adresa uređaja koji je poslao odgovor i 
"OK" ukoliko je odgovor potvrdan(uređaj ima traženu uslugu) ili "FAIL" u suprotnom)...
