<!DOCTYPE html>
<html>
	<head>
		<title>Dynamic DNS</title>
	</head>
	<body>
		<h1>Dynamic DNS</h1>
		<p>
			Create or update a subdomain to name to the caller IP address.
		<p>
		<form action="/subdomain" method="post">
			<fieldset>
				<label>Subdomain Name</label>
				<input type="text" name="name" placeholder="subdomain.name"/>
				<input type="submit" value="Submit"/>
			</fieldset>
		</form>
	</body>
</html>
