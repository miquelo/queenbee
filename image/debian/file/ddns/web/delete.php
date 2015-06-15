<?php
$zone = $_POST['zone'];
$name = $_POST['name'];

$data = "<< EOF
server localhost
zone $zone
update delete $name.$zone A
send
EOF";
exec("/usr/bin/nsupdate -k /etc/bind/ns-ddns_rndc.key $data", $cmdout, $ret);
header('Location: /');
?>

