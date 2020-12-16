import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.ConstantSpeculativeExecutionPolicy;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.policies.LoggingRetryPolicy;

import java.io.File;

public class Main {
    private static Cluster cluster;
    private static Session session;

    public static void main(String[] args) {
        long constantDelayMillis = 1000;
        int maxSpeculativeExecutions =2;
        String username = "test";
        String password = "test";
        cluster = Cluster.builder()
//                .addContactPoints(contactPoints.split(","))
//                .withCloudSecureConnectBundle(new File("/Users/halvorson.jonathon/projects/cb-stack/secure-connect-proxy-internal-cbstack-test.zip"))
                .withCloudSecureConnectBundle(new File("/home/tato/Downloads/secure-connect-proxy-cbstack-test.zip"))
//                .withLoadBalancingPolicy(DCAwareRoundRobinPolicy.builder().build())
//                .withClusterName(clusterName)
//                .withProtocolVersion(protocolVersion)
//                .withLoadBalancingPolicy(
//                        new TokenAwarePolicy(
//                                DCAwareRoundRobinPolicy.builder().withLocalDc(localDatacenter).build()
//                        )
//                )
                .withSpeculativeExecutionPolicy(
                        new ConstantSpeculativeExecutionPolicy(
                                constantDelayMillis,
                                maxSpeculativeExecutions    // maximum number of executions
                        )
                )
                .withRetryPolicy(
                        new LoggingRetryPolicy(
                                DefaultRetryPolicy.INSTANCE
                        )
                )
                .withQueryOptions(
                        new QueryOptions()
                                .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                )
//                .withCompression(ProtocolOptions.Compression.SNAPPY)
                .withCredentials(username, password)
                .build();
        session = cluster.connect();
        System.out.println("connected");
    }
}