package cz.czechitas.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class TestyPrihlasovaniNaKurzy {


    WebDriver prohlizec;

    @BeforeEach
    public void setUp() {
//      System.setProperty("webdriver.gecko.driver", System.getProperty("user.home") + "/Java-Training/Selenium/geckodriver");
        System.setProperty("webdriver.gecko.driver", "C:\\Java-Training\\Selenium\\geckodriver.exe");
        prohlizec = new FirefoxDriver();
        prohlizec.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/");
    }

    //1.Rodič s existujícím účtem se musí být schopen přihlásit do webové aplikace.
    //Poznámka: Nepište automatizovaný test na zakládání nového účtu rodiče. Účet si připravte dopředu
    //ručně.
    @Test
    public void rodicSExistujicimUctemSePrihlasiDoAplikace() throws InterruptedException {

        prihlasRodice("knoblochova.katerina5@gmail.com", "Katka1234");
        ziskejJmenoPrihlasenehoRodiceAOverZeJeRodicPrihlasen("Katka Knoblochová");
    }


    //2. Rodič musí být schopen vybrat kurz, přihlásit se do aplikace a přihlásit na kurz svoje dítě.
    //Poznámka: I zde použijte už existující účet rodiče, jen se k němu v průběhu testu přihlašte.
    //Poznámka: Úspěšné přihlášení dítěte na kurz je třeba po vyplnění přihlášky ověřit ve svém seznamu
    //přihlášek.
    @Test
    public void rodicSExistujicimUctemMuzeVybratKurzPrihlasitSeDoAplikaceAPrihlasitNaKurzSvojeDite() {

        vyberKurz();
        prihlasRodice("knoblochova.katerina5@gmail.com", "Katka1234");
        prihlasDiteNaKurz("01", "Ondra", "Novák", "1.1.2014");
        klikniNaPrehledPrihlasek();
        //na řádcích níž jsem nepřišla na to, jak napsat obecnou funkni, kde by se jen zadal jako parametr jméno dítěte.
        //nevím, ja jinak, než jménem (pomoci Xpath. text = "... ") dtě najít. A jak je celý Xpath String, tak mi nebere proměnnou
        WebElement jmenoPrihlasenehoZaka = prohlizec.findElement(By.xpath("//*[text()='Ondra Novák'] "));
        String strJmenoPrihlasenehoZaka = jmenoPrihlasenehoZaka.getText();

        Assertions.assertEquals("Ondra Novák", strJmenoPrihlasenehoZaka, "Jméno přihlášeného dítěte nesedí");
        odhlasDiteZKurzuKvuliNemoci();
        pockejAzZmiziInfoOdhlaseniZaka();
    }

    //3.Rodič se musí být schopen přihlásit do aplikace, vyhledat kurz a přihlásit na něj svoje dítě.
    //Poznámka: I zde použijte pro přihlášení do aplikace existující účet rodiče a nezapomeňte ověřit, že
    //přihláška na kurz proběhla úspěšně (v rodičově seznamu přihlášek).
    @Test
    public void uzivatelSePrihlasiVyhledaKurzAPrihlasiDite() {
        String datumKurzu = "01";
        String zadávanéJmenoZaka = "Hana";
        String zadávanéPrijmeniZaka = "Knoblochová";
        String zadavaneDatumNarozeniZaka = "1.1.2014";

        prihlasRodice("knoblochova.katerina5@gmail.com", "Katka1234");
        jdiNaHlavniStranku();
        vyberKurz();
        prihlasDiteNaKurz(datumKurzu, zadávanéJmenoZaka, zadávanéPrijmeniZaka, zadavaneDatumNarozeniZaka);
        //na řádcích níž jsem nepřišla na to, jak napsat obecnou funkni, kde by se jen zadal jako parametr jméno dítěte.
        //nevím, ja jinak, než jménem (pomoci Xpath. text = "... ") dtě najít. A jak je celý Xpath String, tak mi nebere proměnnou

        WebElement jmenoPrihlasenehoZaka = prohlizec.findElement(By.xpath("//*[text()='Hana Knoblochová'] "));
        String strJmenoPrihlasenehoZaka = jmenoPrihlasenehoZaka.getText();

        Assertions.assertEquals(zadávanéJmenoZaka + " " + zadávanéPrijmeniZaka, strJmenoPrihlasenehoZaka, "Jméno nesedí");

        odhlasDiteZKurzuKvuliNemoci();
        pockejAzZmiziInfoOdhlaseniZaka();
    }

    @Test
    //4.Rodič se přihlási, přihlásí dítě na kurz a následně změní zdravotní omezení a overi, ze zdravotni omezeni bylo zmeneno
    public void RodicSePrihlasiNaslednePrihlasiDiteNaKurzAPoteUpraviZdravotniOmezeni() {
        String zdravotniOmezeni = "astma";
        prihlasRodice("knoblochova.katerina5@gmail.com", "Katka1234");
        jdiNaHlavniStranku();
        vyberKurz();
        prihlasDiteNaKurz("01", "Petra", "Krátká", "2.3.1989");
        klikniNaTlacitkoUpravit();
        potvrdZdravotniOmezeni();
        vyplnKonkretniZdravotniOmezeni(zdravotniOmezeni);
        potvrdUpraveniPrihlasky();
        WebElement zdravotniOmezeniPrihlasenehoZaka = prohlizec.findElement(By.xpath("//*[text()='astma'] "));
        String strzdravotniOmezeniPrihlasenehoZaka = zdravotniOmezeniPrihlasenehoZaka.getText();
        Assertions.assertEquals(zdravotniOmezeni, strzdravotniOmezeniPrihlasenehoZaka, "Zdravotní omezení žáka nebylo správně upraveno");
        odhlasDiteZKurzuKvuliNemoci();
        pockejAzZmiziInfoOdhlaseniZaka();
    }

    @AfterEach
    public void tearDown() throws InterruptedException {

        odhlasRodice();
        Thread.sleep(2_000);
        prohlizec.quit();
    }

    public void klikniNaTlacitkoUpravit() {
        WebElement upravit = prohlizec.findElement(By.xpath("//*[contains(text(),'Upravit')]"));
        upravit.click();
    }

    public void potvrdZdravotniOmezeni() {
        WebElement potvrzeniZdravotnihoOmezeni = prohlizec.findElement(By.xpath("//*[@for='restrictions_yes']"));
        potvrzeniZdravotnihoOmezeni.click();
    }

    public void klikniNaPrehledPrihlasek() {
        WebElement prehledPrihlasek = prohlizec.findElement(By.xpath("//*[contains(@href,'https://cz-test-jedna.herokuapp.com/zaci')]"));
        prehledPrihlasek.click();

    }

    public void vyplnKonkretniZdravotniOmezeni(String zdravotniOmezeni) {
        WebElement konkretniZdravotnihoOmezeni = prohlizec.findElement(By.xpath("//textarea[@id='restrictions']"));
        konkretniZdravotnihoOmezeni.sendKeys(zdravotniOmezeni);
    }

    public void pockejAzZmiziInfoOdhlaseniZaka() {
        WebDriverWait cekani = new WebDriverWait(prohlizec, 10);
        cekani.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@class='toast-message']")));

    }

    public void potvrdUpraveniPrihlasky() {
        WebElement upravitPrihlasku = prohlizec.findElement(By.xpath("//*[contains(@value,'Upravit přihlášku')]"));
        upravitPrihlasku.click();

    }

    public void prihlasRodice(String email, String heslo) {
        WebElement prihlasitUzivatele = prohlizec.findElement(By.xpath("//*[@href='https://cz-test-jedna.herokuapp.com/prihlaseni']"));
        prihlasitUzivatele.click();
        WebElement poleProVlozeniEmailuUzivatele = prohlizec.findElement(By.id("email"));
        poleProVlozeniEmailuUzivatele.sendKeys(email);
        WebElement poleProVlozeniHeslaUzivatele = prohlizec.findElement(By.id("password"));
        poleProVlozeniHeslaUzivatele.sendKeys(heslo);
        WebElement prihlasitButtonType = prohlizec.findElement(By.xpath("//*[contains(text(),'Přihlásit')]"));
        prihlasitButtonType.click();
    }

    public void prihlasDiteNaKurz(String terminKurzu, String jmeno, String prijmeni, String datumNarozeniDitete) {
        WebElement termin = prohlizec.findElement(By.xpath("//*[text()='Vyberte termín...']"));
        termin.click();
        WebElement searchBox = prohlizec.findElement(By.xpath("//*[@class='form-control']"));
        searchBox.sendKeys(terminKurzu);
        WebElement nabidkaTerminu = prohlizec.findElement(By.xpath("//*[@class='text']"));
        nabidkaTerminu.click();
        WebElement krestniJmenoZaka = prohlizec.findElement(By.id("forename"));
        krestniJmenoZaka.sendKeys(zadejJmeno(jmeno));
        WebElement prijmeniZaka = prohlizec.findElement(By.id("surname"));
        prijmeniZaka.sendKeys(zadejJmeno(prijmeni));
        WebElement datumNarozeni = prohlizec.findElement(By.id("birthday"));
        datumNarozeni.sendKeys(datumNarozeniDitete);
        WebElement platbaBankovnimPrevodem = prohlizec.findElement(By.xpath("//*[text()='Bankovní převod']"));
        platbaBankovnimPrevodem.click();
        WebElement souhlasSPodminkami = prohlizec.findElement(By.xpath("//*[text()='Souhlasím s všeobecnými podmínkami a zpracováním osobních údajů.']"));
        souhlasSPodminkami.click();
        WebElement vytvoritPrihlaseniZaka = prohlizec.findElement(By.xpath("//*[@type = 'submit']"));
        vytvoritPrihlaseniZaka.click();
    }

    public void vyberKurz() {
        WebElement viceInformaciOTrimesicnimKurzu = prohlizec.findElement(By.xpath("(//div[@class = 'card'])[2]//a[text()='Více informací']"));
        viceInformaciOTrimesicnimKurzu.click();
        WebElement vytvoritPrihlasku = prohlizec.findElement(By.xpath("(//div[@class = 'card'])//a[text()='Vytvořit přihlášku']"));
        vytvoritPrihlasku.click();

    }

    public String zadejJmeno(String jmeno) {
        String zadaveneJmeno = jmeno;
        return zadaveneJmeno;
    }

    public void jdiNaHlavniStranku() {
        WebElement domu = prohlizec.findElement(By.xpath("/html/body/div/header/nav/div/div[1]/a[1]"));
        domu.click();

    }

    public void odhlasRodice() {

        WebElement prihlasenyUzivatelJmeno = prohlizec.findElement(By.xpath("//*[@class='dropdown-toggle']"));
        prihlasenyUzivatelJmeno.click();
        WebElement odhlasit = prohlizec.findElement(By.id("logout-link"));
        odhlasit.click();
    }


    public void odhlasDiteZKurzuKvuliNemoci() {
        WebElement odhlaseniZKurzu = prohlizec.findElement(By.xpath("//*[contains(@class,'btn-danger')]"));
        odhlaseniZKurzu.click();
        WebElement duvodNemoc = prohlizec.findElement(By.xpath("//*[text()='Nemoc']"));
        duvodNemoc.click();
        WebElement odhlasitZakaPotvrzeni = prohlizec.findElement(By.xpath("//*[@type = 'submit']"));
        odhlasitZakaPotvrzeni.click();
    }

    public void ziskejJmenoPrihlasenehoRodiceAOverZeJeRodicPrihlasen(String ocekavaneJmenoRodice) {
        WebElement prihlasenyUzivatelJmeno = prohlizec.findElement(By.xpath("//*[@class='dropdown-toggle']"));
        String strJmenoPrihlasenehoUzivatele = prihlasenyUzivatelJmeno.getText();

        Assertions.assertEquals(ocekavaneJmenoRodice, strJmenoPrihlasenehoUzivatele, "Uživatel se nepřihlásil");
    }


}
