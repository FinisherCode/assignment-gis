package sk.finishersapps.finisher.lovemydogo;

import android.content.Intent;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import sk.finishersapps.finisher.lovemydogo.acitivities.MainMenuActivity;

@RunWith(AndroidJUnit4.class)
public class IntegrationTest extends SearchUseCaseTest {

    @Test
    @MediumTest
    public void searchNullTest() throws Throwable {
        Intent i = new Intent(context, MainMenuActivity.class);
        mainMenuRule.launchActivity(i);
        mainMenuRule.getActivity().processSearchResult(null);
    }

    @Test
    @MediumTest
    public void searchBadJsonTest() throws Throwable {
        Intent i = new Intent(context, MainMenuActivity.class);
        mainMenuRule.launchActivity(i);
        mainMenuRule.getActivity().processSearchResult("");
    }

    @Test
    @MediumTest
    public void searchCorrectJsonTest() throws Throwable {
        String fakeResults = "[{\"xCoordinate\":17.1046652,\"yCoordinate\":48.146617,\"name\":\"VET Line\",\"distance\":318.03138252},{\"xCoordinate\":17.1200912,\"yCoordinate\":48.1522315,\"name\":\"Bullypet\",\"distance\":1003.37201996},{\"xCoordinate\":17.0948137,\"yCoordinate\":48.1440285,\"name\":\"Veterinárna poliklinika Sibra\",\"distance\":1088.374011},{\"xCoordinate\":17.114711209567968,\"yCoordinate\":48.157711445193506,\"name\":\"VetLINE\",\"distance\":1138.11504816},{\"xCoordinate\":17.1333238,\"yCoordinate\":48.1490953,\"name\":\"Veterinka\",\"distance\":1903.90962079},{\"xCoordinate\":17.1307225,\"yCoordinate\":48.1594336,\"name\":\"Venusta's\",\"distance\":2091.31620454},{\"xCoordinate\":17.0996858,\"yCoordinate\":48.127096,\"name\":\"Vet\",\"distance\":2465.01860401},{\"xCoordinate\":17.1382133,\"yCoordinate\":48.1594174,\"name\":\"Veterinárna poliklinika\",\"distance\":2566.18134932},{\"xCoordinate\":17.1108018,\"yCoordinate\":48.1214658,\"name\":\"VetPoint\",\"distance\":3025.45949012},{\"xCoordinate\":17.137208313871692,\"yCoordinate\":48.168644365300594,\"name\":\"Veterinárna ambulancia MVDr. Ladislav Šranko\",\"distance\":3126.06205531},{\"xCoordinate\":17.1316745,\"yCoordinate\":48.1205144,\"name\":\"Animavet\",\"distance\":3594.82410858},{\"xCoordinate\":17.1337447,\"yCoordinate\":48.1208905,\"name\":\"VetAnet\",\"distance\":3638.11289238},{\"xCoordinate\":17.1556614,\"yCoordinate\":48.1598116,\"name\":\"Veterinárna ambulancia\",\"distance\":3776.65383758},{\"xCoordinate\":17.057207876867835,\"yCoordinate\":48.152070840121034,\"name\":\"Veterinary clinic\",\"distance\":3780.37806604},{\"xCoordinate\":17.1262517,\"yCoordinate\":48.1111545,\"name\":\"Pet Vet\",\"distance\":4385.3705344},{\"xCoordinate\":17.1638427,\"yCoordinate\":48.1635108,\"name\":\"Veterinárna ambulancia\",\"distance\":4490.80138967},{\"xCoordinate\":17.0457581,\"yCoordinate\":48.1826089,\"name\":\"Veterinárna poliklinika EUVET\",\"distance\":5963.64469412},{\"xCoordinate\":17.037255572542836,\"yCoordinate\":48.19209379219258,\"name\":\"W&G veterinárne združenie\",\"distance\":7133.17879078}]";
        Intent i = new Intent(context, MainMenuActivity.class);
        mainMenuRule.launchActivity(i);
        mainMenuRule.getActivity().processSearchResult(fakeResults);
    }
}
