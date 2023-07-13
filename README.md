# Todo
- Complete tenant creation end to end
- Create users from spring in keyclock
- apply domain
  i wana to send this rest request via spring boot
  http://localhost:9090/realms/master/protocol/openid-connect/token
  POST
  x-www..form data
  client_id = admin-cli
  username = fofo
  password = fofo
  grant_type = password

This will get me {{
"access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIycFk2ZnJQa01xYUlJdDRMOEtubGtJUU1fRDZydV82WU92dDBRdThZN0JjIn0.eyJleHAiOjE2ODkyNTY2MDMsImlhdCI6MTY4OTI1NjMwMywianRpIjoiZjczYmU3NDQtMmZmNS00MzIyLWI1ZWQtZTQ3MWY0NGI1OTA4IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDkwL3JlYWxtcy90ZW5hbnQtMiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiIyZTYzMGVmZS04MjI2LTQxM2YtYTFhNi05YzM3MTFkMWJjZmMiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJhcHAiLCJzZXNzaW9uX3N0YXRlIjoiZDhlMDRkOWEtNDUxNS00OWQ1LTlmMWQtNmZiNGJiZmI2NWNmIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0Il0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJ1c2VyMiIsIm9mZmxpbmVfYWNjZXNzIiwiZGVmYXVsdC1yb2xlcy10ZW5hbnQtMiIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiZDhlMDRkOWEtNDUxNS00OWQ1LTlmMWQtNmZiNGJiZmI2NWNmIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJtaW1pIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.nSokH95pf8GCqLd3oOfA2Dn9WW7HIrlX94qApM_aV70rl3ipSkehZUWepm9ZwBtrg6YtHabiiMFW13Ifla6rQmxetFckQD2fFkdbhBSU89r8hTGM_5TIADKMDe6o6Gh-RQSRw0QKO9j3I8xbrmwII3thkf-h6Vvodms_I7WRu3kbiWBxWA24s0GkXi7l9fuHiIUVtGfJBnSOJH3Ug9KP1hmcnIpg5yxVcYOIz6JWDrW6DHsIlyWYGMx3LKqOqNUzoPKG-mrP7f8UZSyUUG9K7orxhh2-2BJlthV5tTupm8vkShmNoRYdREjve0XSjx21Ejp8AxME20Is86v7bO5xag",
"expires_in": 300,
"refresh_expires_in": 1800}

then i wana extract the token and send another POST to this
http://localhost:9090/admin/realms
with this body
{
"id": "newrealm",
"realm": "newrealm",
"displayName": "New Realm",
"enabled": true,
"sslRequired": "external",
"registrationAllowed": false,
"loginWithEmailAllowed": true,
"duplicateEmailsAllowed": false,
"resetPasswordAllowed": false,
"editUsernameAllowed": false,
"bruteForceProtected": true
}
and Autherisation Bearor header with the gained token