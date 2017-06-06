package mx.com.desoft.hidrogas;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

import java.util.Date;

import mx.com.desoft.hidrogas.bussines.LiquidacionBussines;
import mx.com.desoft.hidrogas.to.LiquidacionesTO;
import mx.com.desoft.hidrogas.to.ViajesTO;
import mx.com.desoft.hidrogas.utils.Utils;

public class PrintActivity extends Activity implements  ReceiveListener {

    private Context mContext = null;
    private Printer  mPrinter = null;

    public boolean runPrintReceiptSequence(Long idLiquidacion,String clave, Context context) {
        mContext = context;
        if (!initializeObject()) {
            return false;
        }

        if (!createReceiptData(idLiquidacion, clave)) {
            finalizeObject();
            return false;
        }

        if (!printData()) {
            finalizeObject();
            return false;
        }

        return true;
    }

    private boolean createReceiptData(Long idLiquidacion,String clave) {
        String method = "";
        LiquidacionBussines liquidacionBussines = new LiquidacionBussines();
        Utils utils = new Utils();
        int cont = 1;

        if (mPrinter == null) {
            return false;
        }

        try {
            LiquidacionesTO liquidacionesTO = liquidacionBussines.getLiquidacionByIdLiquidacion(idLiquidacion);
            liquidacionesTO.setViajes(liquidacionBussines.getViajesByIdLiquidacion(idLiquidacion.intValue()));

            StringBuilder ticket = new StringBuilder();
            ticket.append("FECHA: "+ utils.convertirFecha(new Date().getTime())+ "\n");
            ticket.append("FOLIO: " + idLiquidacion + "\n");
            ticket.append("PIPA: "+liquidacionesTO.getNoPipa().toString() +"\n");
            ticket.append("ECONOMICO: " + liquidacionesTO.getEconomico() +"\n");
            ticket.append("No. NOMINA CHOFER: " + liquidacionesTO.getNominaChofer() + "\n");
            ticket.append("CHOFER: " + liquidacionesTO.getChofer() + "\n");
            ticket.append("No. NOMINA AYUDANTE: " + liquidacionesTO.getNominaAyudante() + "\n");
            ticket.append("AYUDANTE: " + liquidacionesTO.getAyudante() + "\n");
            ticket.append("*************** VIAJES ***************\n");
            for (ViajesTO viajes: liquidacionesTO.getViajes()) {
                ticket.append("VIAJE - " + cont + "\n");
                ticket.append("SALIDA: " + viajes.getPorcentajeInicial() +"\n");
                ticket.append("LLEGADA: " + viajes.getPorcentajeFinal() + "\n");
                ticket.append("TOTALIZADOR INICIAL: " + viajes.getTotalizadorInicial() + "\n");
                ticket.append("TOTALIZADOR FINAL: " + viajes.getTotalizadorFinal() + "\n");
                cont++;
            }
            ticket.append("**************************************\n");
            ticket.append("VARIACION: " +liquidacionesTO.getVariacion() +"\n");
            ticket.append("CLAVE: " + clave +"\n");
            ticket.append("PORCENTAJE VARIACION: " + liquidacionesTO.getPorcentajeVariacion() +"\n");
            method = "addText";
            mPrinter.addText(ticket.toString());

            method = "addCut";
            mPrinter.addCut(Printer.CUT_FEED);
            ticket = null;
        }
        catch (Exception e) {
            ShowMsg.showException(e, method, mContext);
            return false;
        }
        return true;
    }

    private boolean printData() {
        if (mPrinter == null) {
            return false;
        }

        if (!connectPrinter()) {
            return false;
        }

        PrinterStatusInfo status = mPrinter.getStatus();

        dispPrinterWarnings(status);

        if (!isPrintable(status)) {
            ShowMsg.showMsg(makeErrorMessage(status), mContext);
            try {
                mPrinter.disconnect();
            }
            catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        try {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        }
        catch (Exception e) {
            ShowMsg.showException(e, "sendData", mContext);
            try {
                mPrinter.disconnect();
            }
            catch (Exception ex) {
                // Do nothing
            }
            return false;
        }

        return true;
    }

    private boolean initializeObject() {
        try {
            mPrinter = new Printer(Printer.TM_T20, Printer.MODEL_ANK, mContext);
        }
        catch (Exception e) {
            ShowMsg.showException(e, "Printer", mContext);
            return false;
        }

        mPrinter.setReceiveEventListener(this);

        return true;
    }

    private void finalizeObject() {
        if (mPrinter == null) {
            return;
        }

        mPrinter.clearCommandBuffer();

        mPrinter.setReceiveEventListener(null);

        mPrinter = null;
    }

    private boolean connectPrinter() {
        String ip = "TCP:192.168.120.205";
        boolean isBeginTransaction = false;

        if (mPrinter == null) {
            return false;
        }

        try {
            mPrinter.connect(ip, Printer.PARAM_DEFAULT);
        }
        catch (Exception e) {
            ShowMsg.showException(e, "connect", mContext);
            return false;
        }

        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        }
        catch (Exception e) {
            ShowMsg.showException(e, "beginTransaction", mContext);
        }

        if (!isBeginTransaction) {
            try {
                mPrinter.disconnect();
            }
            catch (Epos2Exception e) {
                // Do nothing
                return false;
            }
        }

        return true;
    }

    private void disconnectPrinter() {
        if (mPrinter == null) {
            return;
        }

        try {
            mPrinter.endTransaction();
        }
        catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    ShowMsg.showException(e, "endTransaction", mContext);
                }
            });
        }

        try {
            mPrinter.disconnect();
        }
        catch (final Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public synchronized void run() {
                    ShowMsg.showException(e, "disconnect", mContext);
                }
            });
        }

        finalizeObject();
    }

    private boolean isPrintable(PrinterStatusInfo status) {
        if (status == null) {
            return false;
        }

        if (status.getConnection() == Printer.FALSE) {
            return false;
        }
        else if (status.getOnline() == Printer.FALSE) {
            return false;
        }

        return true;
    }

    private String makeErrorMessage(PrinterStatusInfo status) {
        String msg = "";

        if (status.getOnline() == Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE) {
            msg += getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE) {
            msg += getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY) {
            msg += getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON) {
            msg += getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR) {
            msg += getString(R.string.handlingmsg_err_autocutter);
            msg += getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR) {
            msg += getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR) {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT) {
                msg += getString(R.string.handlingmsg_err_overheat);
                msg += getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER) {
                msg += getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0) {
            msg += getString(R.string.handlingmsg_err_battery_real_end);
        }

        return msg;
    }

    private void dispPrinterWarnings(PrinterStatusInfo status) {
        String warningsMsg = "";

        if (status == null) {
            return;
        }

        if (status.getPaper() == Printer.PAPER_NEAR_END) {
            warningsMsg += getString(R.string.handlingmsg_warn_receipt_near_end);
        }

        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
            warningsMsg += getString(R.string.handlingmsg_warn_battery_near_end);
        }

        //Toast.makeText(getApplicationContext(),warningsMsg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPtrReceive(final Printer printerObj, final int code, final PrinterStatusInfo status, final String printJobId) {
        runOnUiThread(new Runnable() {
            @Override
            public synchronized void run() {
                Toast.makeText(mContext, makeErrorMessage(status),Toast.LENGTH_SHORT);
                //ShowMsg.showResult(code, makeErrorMessage(status), mContext);

                dispPrinterWarnings(status);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        disconnectPrinter();
                    }
                }).start();
            }
        });
    }
}
