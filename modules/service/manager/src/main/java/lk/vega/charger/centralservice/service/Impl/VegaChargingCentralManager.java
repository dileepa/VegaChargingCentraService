package lk.vega.charger.centralservice.service.Impl;

import lk.vega.charger.centralservice.service.*;
import lk.vega.charger.centralservice.service.paymentgateway.DialogEasyCashGateway;
import lk.vega.charger.centralservice.service.paymentgateway.MobitelMCashGateway;
import lk.vega.charger.centralservice.service.paymentgateway.PaymentGateWay;
import lk.vega.charger.centralservice.service.transaction.TransactionController;
import lk.vega.charger.core.ChargeTransaction;

import javax.jws.WebParam;

/**
 * Created with IntelliJ IDEA.
 * User: dileepa
 * Date: 3/18/15
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class VegaChargingCentralManager implements CentralSystemService
{


    public static final String DIALOG_UNIQUE_KEY = "077";
    public static final String MOBITEL_UNIQUE_KEY = "071";


    @Override
    public AuthorizeResponse authorize( @WebParam(name = "authorizeRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") AuthorizeRequest parameters )
    {
        PaymentGateWay paymentGateWay = null;
        String authorizeKey = parameters.getIdTag();
        ChargeTransaction inProgressChargeTransaction = TransactionController.loadProcessingTransaction( authorizeKey, TransactionController.TRS_STARTED );
        AuthorizeResponse authorizeResponse = new AuthorizeResponse();
        if (inProgressChargeTransaction == null)
        {
            if( authorizeKey.startsWith( DIALOG_UNIQUE_KEY ) )
            {
                paymentGateWay = new DialogEasyCashGateway();
            }
            else if( authorizeKey.startsWith( MOBITEL_UNIQUE_KEY ) )
            {
                paymentGateWay = new MobitelMCashGateway();
            }
//            paymentGateWay.validateTransaction( authorizeKey );
            //TODO modify AuthorizeResponse
        }
        else
        {
            inProgressChargeTransaction.setTransactionStatus( TransactionController.TRS_PROCESSED );
            //TODO update query for updating status
            //TODO modify AuthorizeResponse
        }


        return authorizeResponse;
    }

    @Override
    public StartTransactionResponse startTransaction( @WebParam(name = "startTransactionRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") StartTransactionRequest parameters )
    {
        ChargeTransaction chargeTransaction = TransactionController.generateTransaction( parameters );
        StartTransactionResponse startTransactionResponse = new StartTransactionResponse();
        //TODO Mapping StartTransactionResponse to  ChargeTransaction
        return startTransactionResponse;
    }

    @Override
    public StopTransactionResponse stopTransaction( @WebParam(name = "stopTransactionRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") StopTransactionRequest parameters )
    {
        String authorizeKey = parameters.getIdTag();
        ChargeTransaction inProgressChargeTransaction = TransactionController.loadProcessingTransaction( authorizeKey, TransactionController.TRS_PROCESSED );
        StopTransactionResponse stopTransactionResponse = new StopTransactionResponse();
        if (inProgressChargeTransaction != null)
        {
            //TODO update query for updating status
            //TODO modify StopTransactionResponse
        }

        return stopTransactionResponse;
    }

    @Override
    public HeartbeatResponse heartbeat( @WebParam(name = "heartbeatRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") HeartbeatRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MeterValuesResponse meterValues( @WebParam(name = "meterValuesRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") MeterValuesRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BootNotificationResponse bootNotification( @WebParam(name = "bootNotificationRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") BootNotificationRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public StatusNotificationResponse statusNotification( @WebParam(name = "statusNotificationRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") StatusNotificationRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public FirmwareStatusNotificationResponse firmwareStatusNotification( @WebParam(name = "firmwareStatusNotificationRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") FirmwareStatusNotificationRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DiagnosticsStatusNotificationResponse diagnosticsStatusNotification( @WebParam(name = "diagnosticsStatusNotificationRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") DiagnosticsStatusNotificationRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DataTransferResponse dataTransfer( @WebParam(name = "dataTransferRequest", targetNamespace = "urn://Ocpp/Cs/2012/06/", partName = "parameters") DataTransferRequest parameters )
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
