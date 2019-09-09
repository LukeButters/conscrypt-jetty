# How I made the keystore/truststores here - Notes for later...

# See /funnelback-crawler/tests/com/funnelback/crawler/http/okhttp/X509SslTest.java and
# /funnelback-crawler/tests/com/funnelback/crawler/http/okhttp/SslTest.java

# First, clean up an old versions - You can just run this file with `source readme.txt` to automate if it.

rm client-* server-*


# For an SSL server with a self-signed cert the client must trust, so we make a keystore for the server,
# and a truststore for the client

# Spark framework (the test server) seems to require that the keypass and storepass be the same
# for some reason - That doesn't seem right/necessary, but maybe they have a reason.

keytool -genkey -dname "CN=127.0.0.1, OU=rnd, O=funnelback, L=canberra, ST=act, C=au" -ext "san=ip:127.0.0.1" -alias "client-alias" -keyalg RSA -validity 3650 -storepass serverkeypass -keypass serverkeypass -keystore server-keystore.jks

keytool -exportcert -alias "client-alias" -storepass serverkeypass -keypass serverkeypass -keystore server-keystore.jks > server-certificate.p12

keytool -import -trustcacerts -alias "client-alias" -file server-certificate.p12 -noprompt -keypass serverkeypass -storepass clientrustpass -keystore client-truststore.jks



# For an x.509 client certificate, that the server must trust a client certificate - make a client keystore 
# for the client's certificate, and a truststore for the server (which says to trust the client).

keytool -genkey -dname "CN=matt, OU=rnd, O=funnelback, L=canberra, ST=act, C=au" -alias "client-alias" -keyalg RSA -validity 3650 -storepass clientstoresecret -keypass clientkeysecret -keystore client-keystore.jks

keytool -exportcert -alias "client-alias" -storepass clientstoresecret -keypass clientkeysecret -keystore client-keystore.jks > client-certificate.p12

keytool -import -trustcacerts -alias "client-alias" -file client-certificate.p12 -noprompt -keypass clientkeysecret -storepass servertrustpass -keystore server-truststore.jks


# If you want to test with something other than java, you make be able to make files for curl with something like
#
# keytool -importkeystore -srckeystore client-keystore.jks -srcalias "client-alias" \
#  -srcstorepass keystoresecret -srckeypass keysecret -destkeystore client-keystore-for-curl.pfx -deststoretype PKCS12 \
#  -deststorepass clientsecret -destkeypass clientsecret
#
# openssl pkcs12 -in client-keystore-for-curl.pfx -out client-keystore-for-curl-unencrypted.p12 -nodes -passin pass:clientsecret
