package harvesterUI.server.externalServices;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import harvesterUI.client.servlets.externalServices.ESManagementService;
import harvesterUI.server.util.Util;
import harvesterUI.shared.ServerSideException;
import harvesterUI.shared.externalServices.ExternalServiceUI;
import harvesterUI.shared.externalServices.ServiceParameterUI;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import pt.utl.ist.repox.externalServices.ExternalRestService;
import pt.utl.ist.repox.externalServices.ServiceParameter;
import pt.utl.ist.repox.util.ConfigSingleton;
import pt.utl.ist.repox.util.XmlUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ESManagementServiceImpl extends RemoteServiceServlet implements ESManagementService {

    public ESManagementServiceImpl() {}

    /**
     *External Rest Services
     **/
    public List<ExternalServiceUI> getAllExternalServices() throws ServerSideException {
        List<ExternalServiceUI> servicesList = new ArrayList<ExternalServiceUI>();
        int parameterID = 0;

        try {
            for(ExternalRestService externalRestService : ConfigSingleton.getRepoxContextUtil().getRepoxManager().getExternalRestServicesManager().getExternalRestServices()){
                List<ServiceParameterUI> serviceParameters = new ArrayList<ServiceParameterUI>();
                for(ServiceParameter serviceParameter : externalRestService.getServiceParameters()){
                    ServiceParameterUI serviceParameterUI = new ServiceParameterUI(serviceParameter.getName(),
                            serviceParameter.getType(),serviceParameter.getRequired(),serviceParameter.getExample(),
                            serviceParameter.getSemantics());
                    if(serviceParameter.getType().equals("COMBO_FIELD"))
                        serviceParameterUI.setComboValues(serviceParameter.getComboValues());

                    serviceParameterUI.setId(String.valueOf(parameterID));
                    serviceParameters.add(serviceParameterUI);
                    parameterID++;
                }

                ExternalServiceUI externalServiceUI = new ExternalServiceUI(externalRestService.getId(),
                        externalRestService.getName(),externalRestService.getUri(),externalRestService.getStatusUri(),
                        externalRestService.getType(),serviceParameters);
                servicesList.add(externalServiceUI);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerSideException(Util.stackTraceToString(e));
        }
        return servicesList;
    }

    public String removeExternalService(List<ExternalServiceUI> externalServiceUIs) throws ServerSideException{
        try {
            File externalServicesFile = new File(ConfigSingleton.getRepoxContextUtil().getRepoxManager().getConfiguration().getXmlConfigPath() + File.separator + "externalServices.xml");

            if(!externalServicesFile.exists())
                return "ERROR";

            SAXReader reader = new SAXReader();
            Document document = reader.read(externalServicesFile);

            List list = document.selectNodes("//restServices/restService");

            for(ExternalServiceUI externalServiceUI : externalServiceUIs){
                for(Object node: list){
                    Node n = (Node) node;
                    String serviceID = n.valueOf("@id");
                    if(serviceID.equals(externalServiceUI.getId()))
                        n.detach();
                }
            }

            XmlUtil.writePrettyPrint(externalServicesFile, document);

            // Reload data on core
            ConfigSingleton.getRepoxContextUtil().getRepoxManager().getExternalRestServicesManager().loadExternalRestServices();

            return "SUCCESS";
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerSideException(Util.stackTraceToString(e));
        }
    }

    public String saveExternalService(boolean isUpdate, ExternalServiceUI externalServiceUI) throws ServerSideException{
        try {
            File externalServicesFile = new File(ConfigSingleton.getRepoxContextUtil().getRepoxManager().
                    getConfiguration().getXmlConfigPath() + File.separator + "externalServices.xml");
            Document document;
            if(!externalServicesFile.exists()){
                document = DocumentHelper.createDocument();
                document.addElement("restServices");
            }else{
                SAXReader reader = new SAXReader();
                document = reader.read(externalServicesFile);
            }

            if(isUpdate){
                List<Node> list = document.selectNodes("//restServices/restService");
                for(Node serviceNode: list){
                    String serviceID = serviceNode.valueOf("@id");
                    if(serviceID.equals(externalServiceUI.getId())){
                        serviceNode.detach();
                    }
                }
            }

            Element externalServiceNode = document.getRootElement().addElement("restService");
            externalServiceNode.addAttribute("id",externalServiceUI.getId());
            externalServiceNode.addAttribute("name",externalServiceUI.getName());
            externalServiceNode.addAttribute("uri",externalServiceUI.getUri());
            externalServiceNode.addAttribute("statusUri",externalServiceUI.getStatusUri());
            externalServiceNode.addAttribute("type",externalServiceUI.getType());
            Element serviceParameters = externalServiceNode.addElement("parameters");
            for(ServiceParameterUI serviceParameter : externalServiceUI.getServiceParameters()){
                Element serviceParameterNode = serviceParameters.addElement("parameter");
                serviceParameterNode.addAttribute("name",serviceParameter.getName());
                serviceParameterNode.addAttribute("type",serviceParameter.getType());
                serviceParameterNode.addAttribute("required",String.valueOf(serviceParameter.getRequired()));
                serviceParameterNode.addAttribute("example",serviceParameter.getExample());
                serviceParameterNode.addAttribute("semantics",serviceParameter.getSemantics());
                if(serviceParameter.getType().equals("COMBO_FIELD")){
                    Element comboValuesElement = serviceParameterNode.addElement("comboValues");
                    for(String comboValue : serviceParameter.getComboValues()){
                        Element comboValueElement = comboValuesElement.addElement("comboValue");
                        comboValueElement.setText(comboValue);
                    }
                }
            }

            XmlUtil.writePrettyPrint(externalServicesFile, document);

            // Reload data on core
            ConfigSingleton.getRepoxContextUtil().getRepoxManager().getExternalRestServicesManager().loadExternalRestServices();
            return "SUCCESS";
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerSideException(Util.stackTraceToString(e));
        }
    }
}
