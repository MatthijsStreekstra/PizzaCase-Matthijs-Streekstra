PizzaCase project - Matthijs Streekstra

Omschrijving:
Toepassing waarbij je online één of meer pizza's kan bestellen. Hierbij is er een server (de pizza leverancier) en een client (de besteller), waarbij de communicatie (de bestelling) plaats vindt op basis van een bericht waarin de bestelling vastgelegd is. De manier van communicatie is TCP want TCP garandeert dat alle verzonden gegevens correct aankomen, zonder verlies of duplicatie. TCP vereist ook dat er een actieve verbinding is tussen client en server voordat gegevens kunnen worden uitgewisseld. Daarnaast is het verschil tussen TCP en UDP dat bij TCP wederzijdse communicatie is (sleuteluitwisseling), en bij UDP alleen 'eenrichtingsverkeer'.
UDP is ook toegevoegd als extra keuze.

De verandering in de huidige versie is dat de bestelling niet als een byte arrray verstuurd wordt, maar als een string. Daarnaast is er een optie om de netwerkcommunicatie te veranderen tussen 2 opties.

Eisen met betrekking tot verschillende gebieden:
-Security van de bestelling 
-Gebruik van Design Patterns
-Manier waarop de communicatie plaatsvindt.

Security Patterns
De volgende security design patters komen terug:

Diffie-Hellmann sleuteluitwisseling
Zowel de client als de server genereren een KeyPair en wisselen hun publieke sleutels uit. Na het ontvangen van de publieke sleutel van de andere partij, genereert elk van hen dezelfde gedeelde geheime bytes die worden gebruikt om een AES sleutel (secretKeySpec) te maken. de client serialiseert en versleutelt de bestelling met de gedeelde geheime sleutel, en stuurt deze vervolgens naar de server. De server ontvangt de versleutelde bestelling, ontsleutelt deze met de gedeelde geheime sleutel, en deserialiseert deze om de oorspronkelijke bestelling terug te krijgen. Vervolgens wordt de bestelling teogevoegd aan de 'orderProcessor' voor verdere verwerking.

Secure Proxy-pattern
Dit design pattern wordt gebruikt om toegang tot gevoelige onderdelen te controleren en beheren. In het geval van de Pizza Case, is de server kant een soort secure proxy. Client verstuurt versleutelde gegevens naar de server en de server is de enige die de sleutel heeft zodat de server de gegevens kan zien. Dit houdt in dat de server kan beheren wie de ontsleutelde gegevens kan inzien.

Design Patterns
De volgende Patterns komen terug: 

Singleton
Dit zorgt ervoor dat er slechts één instantie van een klasse bestaat en biedt een wereldwijde toegangspunt tot deze instantie. In dit geval is het Singleton design pattern gebruikt in PizzaServer.java. Dit betekent dat er slechts één instantie van elk zal bestaan gedurende de levensduur van de applicatie. Dit is terug te zien in App.java en in PizzaServer.java aangezien de constructor private is gemaakt, dit is gedaan om te voorkomen dat er ergens anders in de code een nieuwe instantie van de code wordt gemaakt. De server is de enige die maar één keer aangemaakt hoeft te worden, aangezien er meerdere clients kunnen zijn.

Composite
CompositeOperation: deze klasse vertegenwoordigd een samengestelde operatie die zowel basic serialization als basic encrpytion kan bevatten. Hierdoor kunnen meerdere operaties sequentieel worden uitgevoerd als één enkele operatie.
BasicSerialization en BasicEncryption: Dit zijn de leaf-components (klassen) die individuele operaties uitvoeren zoals eerder beschreven.
PizzaClient: In deze klasse voegen we de operaties toe aan een CompositeOperation, deze voert vervolgens de operaties BasicEncryption en BasicSerialization uit.

Visitor
Deze pattern wordt gebruikt om operaties toe te passen aan groep gerelateerde objecten zonder de structuur van de klassen te wijzigen. In de applicatie kan deze Design Pattern gebruikt worden om verschillende functies uit te voeren op de pizza objecten. In dit geval is dat bijvoorbeeld de pizza's toevoegen aan een bestelling, de extra toppings toevoegen en laten zien aan de server-kant. Toekomstige functionaliteiten zijn bijvoorbeeld het berekenen van de totaalprijs of de ingredienten laten zien van de pizza's. Dit is handig aangezien de mate van schaalbaarheid nu makkelijker te vergroten is.
