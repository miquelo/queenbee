<!--

  This file is part of QueenBee Project.
 
  QueenBee Project is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
 
  QueenBee Project is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
 
  You should have received a copy of the GNU General Public License
  along with QueenBee Project.  If not, see <http://www.gnu.org/licenses/>.
  
-->
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
				<label for="sdNameField">Subdomain name</label>
				<input if="sdNameField" type="text" name="sdname" placeholder="subdomain.name"/>
				<input type="submit" value="Update"/>
			</fieldset>
		</form>
	</body>
</html>

