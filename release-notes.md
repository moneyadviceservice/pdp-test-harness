# Release Notes

The following document shows the changes introduced in each version of the C&A Stub.

## v1.1.5 - 27 Sep 2024
* Fix the regex pattern for the `name` parameter in the `rreguri` API
* Fix logic for the valid deletion reasons depending on the match status
* Correct typo on "pension_dashboard_rqp" on the `token` API

## v1.1.4 - 23 Aug 2024
* Fix bug on log when a parameter value is null
* 429 response to include “Retry-after” header.

## v1.1.3 - 16 Aug 2024
* Implemented error responses to support testing of error scenarios
* Implemented mTLS support

## v1.1.2 - 16 Jul 2024

* Fix Swagger model serialising `CasValidationError` on the error responses
* Fix /jwk_uri tagging

## v1.1.1 - 15 Jul 2024

* Add `/jwk_uri` endpoint

## v1.1.0, 12-jul-2024

* Initial version
