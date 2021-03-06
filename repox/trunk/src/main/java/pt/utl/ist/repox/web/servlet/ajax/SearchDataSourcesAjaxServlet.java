package pt.utl.ist.repox.web.servlet.ajax;

import pt.utl.ist.repox.data.AggregatorRepox;
import pt.utl.ist.repox.data.DataProvider;
import pt.utl.ist.repox.data.DataSource;
import pt.utl.ist.repox.util.RepoxContextUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;


/* ------------------------------------------------------------ */
/** Dummy Servlet Request.
 *
 */
public class SearchDataSourcesAjaxServlet extends HttpServlet {
    private static final String SEPARATOR = "|";
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger( SearchDataSourcesAjaxServlet.class);
    /* ------------------------------------------------------------ */
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
    }

    /* ------------------------------------------------------------ */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doGet(request, response);
    }

    /* ------------------------------------------------------------ */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String searchString = request.getParameter("q");
        String limitString = request.getParameter("limit");
        log.debug("searchString q: " + searchString);

        response.setContentType("text/plain");
//    	response.setCharacterEncoding(System.getProperty("file.encoding"));
        //response.setCharacterEncoding("ISO-8859-1");
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream out = response.getOutputStream();

        try {
            List<AggregatorRepox> aggregators = RepoxContextUtil.getRepoxManager().getDataManager().loadAggregatorsRepox();

            for (AggregatorRepox aggregatorRepox : aggregators) {

                // todo adicionar os aggregators
                String aggSearchString = aggregatorRepox.getId() + " " + aggregatorRepox.getName() + " "
                        + (aggregatorRepox.getNameCode() != null ? aggregatorRepox.getNameCode() : "")
                        + (aggregatorRepox.getHomePage() != null ? aggregatorRepox.getHomePage() : "");

                if(isResultValid(searchString, aggSearchString)) {
                    String row = getResultRow("ag", aggregatorRepox.getId(), aggregatorRepox.getName(), null, null, null, null);
                    out.println(row);
                }

                for (DataProvider dataProvider : aggregatorRepox.getDataProviders()) {
//				dp: "type"|"dp.id"|"dp.name"|"dp.descr"
//				ds: "type"|"dp.id"|"dp.name"|"ds.descr"|"ds.id"
                    String dpSearchString = dataProvider.getId() + " " + dataProvider.getName() + " "
                            + (dataProvider.getDescription() != null ? dataProvider.getDescription() : "");

                    if(isResultValid(searchString, dpSearchString)) {
                        String row = getResultRow("dp", aggregatorRepox.getId(), aggregatorRepox.getName(), dataProvider.getId(), dataProvider.getName(), dataProvider.getDescription(), null);
                        out.println(row);
                    }

                    if(dataProvider.getDataSources() != null) {
                        for (DataSource dataSource : dataProvider.getDataSources()) {
                            String dsSearchString = dataProvider.getId() + " " + dataProvider.getName() + " "
                                    + (dataSource.getDescription() != null ? dataSource.getDescription() : "") + " " + dataSource.getId();

                            if(isResultValid(searchString, dsSearchString)) {
                                String row = getResultRow("ds",  aggregatorRepox.getId(), aggregatorRepox.getName(), dataProvider.getId(), dataProvider.getName(), dataSource.getDescription(),
                                        dataSource.getId());
                                out.println(row);
                            }
                        }
                    }
                }

            }


        }
        catch (Exception e) {
            log.error("Error loading Data Providers", e);
        }

        out.flush();
    }

    private String getResultRow(String type, String aggregatorId, String aggregatorName, String dataProviderId,
                                String dataProviderName, String description, String dataSourceId)
            throws UnsupportedEncodingException {
        String resultRow = type + SEPARATOR;
        resultRow += aggregatorId + SEPARATOR;
        resultRow += aggregatorName + SEPARATOR;
        resultRow += (dataProviderId == null ? "" : dataProviderId) + SEPARATOR;
        resultRow += (dataProviderName == null ? "" : dataProviderName) + SEPARATOR;
        resultRow += (description == null ? "" : description) + SEPARATOR;
        resultRow += (dataSourceId == null ? "" : dataSourceId) + SEPARATOR;

        return resultRow;
    }

    private boolean isResultValid(String searchString, String dataString) {
        boolean isValid = dataString.matches(".*(?i)" + searchString + ".*");
        return isValid;
    }

}
