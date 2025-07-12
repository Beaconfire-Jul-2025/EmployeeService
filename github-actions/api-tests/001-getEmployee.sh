echo "Testing POST /employee/{id}"

GET_RESPONSE=$(curl -s -X GET \
  "${BASE_URL}/employee/emp001")

if [ -n "$GET_RESPONSE" ]; then
  echo "GET /employee/emp001 successful:"
  echo "$GET_RESPONSE"
  # Add assertions here to validate the content of GET_RESPONSE
  # For example, check for specific text or JSON fields:
  # if echo "$GET_RESPONSE" | grep -q "emp001"; then
  #   echo "Response contains expected employee ID."
  # else
  #   echo "Error: Response does not contain expected employee ID."
  #   exit 1
  # fi
else
  echo "Error: GET /employee/emp001 failed or returned empty response."
  exit 1
fi