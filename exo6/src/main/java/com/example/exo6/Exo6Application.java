package com.example.exo6;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.List;

public class Exo6Application 
{
    public static void main(String[] args) {
        // Configuration du WebDriver
        //System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        WebDriver driver = new ChromeDriver();

        try {
            // Ouvrir le navigateur et naviguer vers Google
            driver.get("https://www.google.com");

            // Attendre que la page soit complètement chargée
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));

            // Accepter les cookies si le bouton est présent
            try {
                WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Tout accepter']/..")));
                acceptButton.click();
            } catch (Exception e) {
                System.out.println("Le bouton 'Tout accepter' n'a pas été trouvé.");
            }

            // Effectuer une recherche pour "automatisation des tests logiciels"
            WebElement searchBox = driver.findElement(By.name("q"));
            searchBox.sendKeys("automatisation des tests logiciels");
            searchBox.submit();

            // Attendre que les résultats de recherche apparaissent
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search")));

            // Vérifier que le titre de la page contient "automatisation des tests logiciels"
            String pageTitle = driver.getTitle();
            assertTrue(pageTitle.contains("automatisation des tests logiciels"));

            // Vérifier que les résultats contiennent le terme "automatisation des tests logiciels"
            List<WebElement> searchResults = driver.findElements(By.cssSelector("h3"));
            boolean found = false;
            for (WebElement result : searchResults) {
                if (result.getText().toLowerCase().contains("automatisation des tests logiciels")) {
                    found = true;
                    break;
                }
            }
            assertTrue("Le terme 'automatisation des tests logiciels' n'a pas été trouvé dans les résultats de recherche.", found);

            // Vérifier la présence de certains éléments spécifiques dans les résultats de recherche
            WebElement firstResult = driver.findElement(By.cssSelector("#search .g"));
            assertNotNull("Le premier résultat de recherche n'est pas trouvé.", firstResult);

            // Message indiquant que tout le test s'est bien passé
            System.out.println("Le test s'est déroulé avec succès.");
        } finally {
            // Fermer le navigateur
            driver.quit();
        }
    }
}
