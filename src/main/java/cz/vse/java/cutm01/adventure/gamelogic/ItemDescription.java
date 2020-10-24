package cz.vse.java.cutm01.adventure.gamelogic;

import java.util.HashMap;
import java.util.Map;

/**
 * Game items (which player can discover during game) with their descriptions which are shown to
 * player after performing OverlookItem command
 *
 * @author Cúth Michal (xname: cutm01)
 * @version spring semester 2019/2020
 */
public enum ItemDescription {
    BOTTLE(
        "Vyzerá to ako obyčajná fľaša s vodou, ktorou by šlo uhasiť smäd a možno aj menší požiar"),
    PEN(
        "Tvoje pero pre šťastie, pomôže pri náročnej skúške ale vďaka ostrému hrotu aj ako potenciálna útočná zbraň"),
    WALLET(
        "Tvoj vianočný darček, síce sa už na nej podpísal zub času, ale doklady a peniaze ešte stále unesie"),
    ID_CARD(
        "Občiansky preukaz z času tvojich 15. narodenín, dnes ti už nikto neuverí, že na fotke si naozaj ty"),
    MONEY("Nejaké drobné, nie veľa, ale na nákup kávy alebo bagety z automatu to bohate postačí"),
    MEDICAL_MASK(
        "Obyčajné chirurgické rúško, zrejme niekomu upadlo, no vyzerá, že by mohlo ešte poslúžiť na ochranu tváre"),
    ROPE(
        "Poctivé horolezecké lano, vyzerá, že by udržalo váhu pri zlaňovaní, ale aj neposlušnú osobu pokiaľ ju zviažeš"),
    ISIC("Tvoj ISIC, môžeš pomocou neho pristupovať do rôznych učební a prednáškových miestností"),
    SMALL_SNACK("Bageta s kuracími nugetkami, síce malá, no na malú chvíľu dokáže zasýtiť"),
    BIG_SNACK(
        "Kuracie stripsy s ľadovým šalátom na jemnom dresingu, tie ti vždy dokázali zahnať hlad"),
    FIRE_EXTINGUISHER(
        "Celkom ťažký hasiaci prístroj, najradšej by si ním rozbil hlavu osobe, ktorá tu bombu odpálila"),
    PROTECTIVE_MEDICAL_SUIT(
        "Opustený celotelový lekársky oblek, hlavne že zdravotníci nemajú dostatok ochranných prostriedkov"),
    JACKET(
        "Vyzerá to ako obyčajná bunda, ale pozeráš, že má celkom veľké vrecká, neskrývajú niečo?"),
    MUSIC_CD(
        "Dlouhá noc a iné exkluzívne hity od českej popovej kráľovnej Helenky Vondráčkovej na tomto CD!"),
    BOOK("Kniha s algoritmami z oblasti umelej inteligencie, povinná výbava každého geeka"),
    STOLEN_WALLET("Cudzia peňaženka, ktorú si tu niekto zabudol, čo to z nej trčí? Sú to peniaze?"),
    STOLEN_MONEY("Veľký obnos peňazí, neprekvapilo by ťa, keby sa nimi niekto snažil o úplatok"),
    STOLEN_ISIC("ISIC študenta, ktorý si ho tu zabudol, môžeš pomocou neho pristupovať do učební"),
    KEYS(
        "Veľký zväzok kľúčov, ktoré nevyzerajú, že by boli zrovna od reťaze na bicykel či iného nedôležitého objektu"),
    PAPER_CLIP(
        "Obyčajná kancelárska spinka, spomínaš si na video ako pomocou nej niekto otvoril zamknuté dvere");

    private final String itemDescription;
    private static final Map<String, String> ITEM_DESCRIPTIONS = new HashMap<>();

    static {
        for (ItemDescription i : values()) {
            ITEM_DESCRIPTIONS.put(i.toString(), i.itemDescription);
        }
    }

    ItemDescription(final String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public static String getItemDescription(String itemName) {
        return ITEM_DESCRIPTIONS.get(itemName);
    }
}
