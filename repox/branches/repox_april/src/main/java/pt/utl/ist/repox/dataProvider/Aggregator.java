package pt.utl.ist.repox.dataProvider;

import eu.europeana.repox2sip.Repox2Sip;
import eu.europeana.repox2sip.dao.Repox2SipImpl;
import eu.europeana.repox2sip.models.Provider;

import java.net.URL;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Emanuel
 * Date: 11/Abr/2010
 * Time: 11:53:20
 */
public class Aggregator {

    private Long id = -1L;

    private String name;

    private URL homePage;

    private String repoxAggregatorId;

    private List<DataProvider> providers;

    /**
     * Get the id generated by the Repox
     *
     * @return String
     */
    public String getRepoxAggregatorId() {
        return repoxAggregatorId;
    }

    /**
     * Set the id generated by the Repox
     *
     * @param repoxAggregatorId String
     */
    public void setRepoxAggregatorId(String repoxAggregatorId) {
        this.repoxAggregatorId = repoxAggregatorId;
    }

    /**
     * Get the List of Providers belonging to this Aggregator instance.
     *
     * @return -  List<DataProvider>
     * @see {@link DataProvider}
     */
    public List<DataProvider> getProviders() {
        return providers;
    }

    /**
     * Set the List of Providers belonging to this Aggregator instance.
     *
     * @param providers - List<DataProvider>
     * @see {@link DataProvider}
     */
    public void setProviders(List<DataProvider> providers) {
        this.providers = providers;
        //TODO: REPOX2SIP add providers to DB
        Repox2Sip repox2sip = new Repox2SipImpl();
        for(DataProvider dp : providers ){
            Provider provider = new Provider();
            //TODO: REPOX2SIP fill provider
            provider.setName(dp.getName());
            //provider.setCountry(dp.getCountry());

            repox2sip.addProvider(provider);
        }

    }

    /**
     * Get the Identifier of the Aggregator. The Identifier value is generated by the server side application. If
     * an error occurs on the server side (e.g. "Aggregator Already Exists"), the default value (-1L) is returned.
     *
     * @return - Long
     */
    public Long getId() {
        return id;
    }


    /**
     * Set the Identifier of the Aggregator. This method is used by the Server side application. A value set by the
     * client side application is ignored.
     *
     * @param id - Long
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the Name of the Aggregator
     *
     * @return - String
     */
    public String getName() {
        return name;
    }

    /**
     * Set the Name of the Aggregator
     *
     * @param name - String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the URL of the Aggregator's Home Page
     *
     * @return URL
     */
    public URL getHomePage() {
        return homePage;
    }

    /**
     * Set the URL of the Aggregator's Home Page
     *
     * @param homePage - URL
     */
    public void setHomePage(URL homePage) {
        this.homePage = homePage;
    }
}

