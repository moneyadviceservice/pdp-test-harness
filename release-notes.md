# Release Notes

The following document shows the changes introduced in each version of the C&A Stub.

## v2.0.3 - 19 May 2026
* Updated Spring and various dependencies to address published security vulnerabilities.

## v2.0.2 - 27 Apr 2026
* Updated embedded Tomcat to address published security vulnerabilities.

## v2.0.1 - 22 Apr 2026
* Fixed bug that caused reporting endpoints to incorrectly reject requests where `submission_id` was present with an explicit `null` value.

## v2.0.0 - 24 Mar 2026
* Added 6 reporting endpoints: `POST /view-response/request-number`, `POST /view-response/response-time`, `POST /view-response/calculations`, `POST /view-response/unavailable`, `POST /service-availability/find`, `POST /service-availability/view`
* Added stub configuration endpoint: `POST /configuration`
* Fixed `DELETE /rreguri/{resource_id}`: `asset-removed` deletion reason is now accepted for resources in `possible` match status, not only `full` matches
* `resource_id` now returns a UUID with an extra trailing digit to match real C&A behaviour

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
