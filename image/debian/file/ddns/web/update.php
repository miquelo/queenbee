<?php
$zone = $_POST['zone'];
$name = $_POST['name'];
$ip = escapeshellcmd($_POST['ip']);

$data = "<< EOF
server localhost
zone $zone
update delete $name.$zone A
update add $name.$zone 300 A $ip
send
EOF";
exec("/usr/bin/nsupdate -k /etc/bind/ns-ddns_rndc.key $data", $cmdout, $ret);
header('Location: /');
?>

