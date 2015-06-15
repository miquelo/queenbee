<!DOCTYPE html>
<html>
	<head>
		<title>Dynamic DNS</title>
	</head>
	<body>
		<h1>Dynamic DNS</h1>
		<p>
			Update a domain with the caller IP address.
		<p>
		<form action="/update" method="post">
			<fieldset>
				<label for="zoneField">Zone</label>
				<input id="zoneField" type="text" name="zone" placeholder="domain.name"/>
				<label for="nameField">Subdomain name</label>
				<input if="nameField" type="text" name="name" placeholder="subdomain.name"/>
				<label for="ipField">IP address</label>
				<input if="ipField" type="text" name="ip" placeholder="192.168.11.4"/>
				<input type="submit" value="Update"/>
			</fieldset>
		</form>
	</body>
</html>

