package LISTENER;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class Listener extends TestListenerAdapter {

    public ExtentHtmlReporter extentHtmlReporter;
    public ExtentReports extentReport;
    public ExtentTest extentTest;

    @Override
    public void onStart(ITestContext testContext){
        //yeni Html Reporter object yaratıp , htmlreporter'ın path'ini belirtiyorum
        extentHtmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") +"/Reports/Report.html");

        //Reporter için Dokuman adı- Rapor adı ve Theme konfigurasyonalarını yapıyorum
        extentHtmlReporter.config().setDocumentTitle("API CAMPUS TESTING REPORT");
        extentHtmlReporter.config().setReportName("API CAMPUS TESTING REPORT");
        extentHtmlReporter.config().setTheme(Theme.STANDARD);


        //yeni Report Objesi yaratıp raporu reportera attach ediyorum
        //Proje adı,   Host name,   Environment,  user bilgilerini sisteme tanımlıyorum
        extentReport= new ExtentReports();
        extentReport.attachReporter(extentHtmlReporter);
        extentReport.setSystemInfo("Project Name","CAMPUS REST API TESTING");
        extentReport.setSystemInfo("Host Name","11.111.111.111");
        extentReport.setSystemInfo("Environment","Test");
        extentReport.setSystemInfo("User","Tester");
    }

    @Override
    public void onTestSuccess(ITestResult result){
    //Success eden test için createTest metoduyla yeni object yaratıyorum ve result parametremin adını çekiyorum
        extentTest = extentReport.createTest(result.getName());
        extentTest.log(Status.PASS,result.getName() + ">> PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result){
        //Fail eden test için createTest metoduyla yeni object yaratıyorum ve result parametremin adını çekiyorum
        //getThrowable metoduyla fail için raporda hata versin diyorum
        extentTest = extentReport.createTest(result.getName());
        extentTest.log(Status.FAIL, result.getName() +">> FAILED");
        extentTest.log(Status.FAIL,result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result){
        extentTest= extentReport.createTest(result.getName());
        extentTest.log(Status.SKIP, result.getName() +">> SKIPPED");
    }

    @Override
    public void onFinish(ITestContext testContext){

        extentReport.flush();
    }
}
