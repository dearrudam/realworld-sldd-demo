/// # Users
/// > Manage user accounts, authenticated user identity, public profiles, and following relationships.
///
/// ## Boundary
/// - `register-user` — create a user account from username, email, and password
/// - `authenticate-user` — establish authenticated identity from user credentials
/// - `get-current-user` — return the authenticated user's account representation
/// - `update-current-user` — change the authenticated user's account and profile details
/// - `view-profile` — return a user's public profile by username
/// - `follow-user` — make the authenticated user follow another user
/// - `unfollow-user` — make the authenticated user stop following another user
///
/// ## Requirements
/// ### R1: Register a user
/// - R1.1 — When a username, email, and password are submitted and each value is non-blank and unique where required, the BC shall create a user account and return authenticated user identity.
/// - R1.2 — If a submitted registration omits username, email, or password, then the BC shall reject the registration.
/// - R1.3 — If a submitted username, email, or password is blank, then the BC shall reject the registration.
/// - R1.4 — If the submitted username or email is already used by another user, then the BC shall reject the registration.
/// - R1.5 — When a registration with a password is accepted, the BC shall retain credential verification material and shall not retain the submitted plaintext password. _(why: accepted credentials must not expose reusable secrets at rest)_
/// - R1.6 — When a registration is accepted, the BC shall return authenticated user identity with a JWT token.
/// - R1.7 — When a registration is accepted, the BC shall durably retain the created user account, credential verification material, and public profile.
///
/// ### R2: Authenticate a user
/// - R2.1 — When valid email and password credentials are submitted, the BC shall return authenticated user identity.
/// - R2.2 — If submitted credentials are missing, blank, or invalid, then the BC shall reject authentication.
/// - R2.3 — When authentication is accepted, the BC shall return authenticated user identity with a JWT token.
///
/// ### R3: Get the current user
/// - R3.1 — While the caller has valid credentials, when the current user is requested, the BC shall return the authenticated user's account representation.
/// - R3.2 — If the caller lacks valid credentials, then the BC shall reject the current-user request.
/// - R3.3 — If the caller supplies a credential that is not a valid JWT token, then the BC shall reject the current-user request.
///
/// ### R4: Update the current user
/// - R4.1 — While the caller has valid credentials, when account or profile details are submitted, the BC shall update the supplied details and preserve omitted details.
/// - R4.2 — If a submitted update detail is blank, then the BC shall reject the update.
/// - R4.3 — If a submitted username or email is already used by another user, then the BC shall reject the update.
/// - R4.4 — If the caller lacks valid credentials, then the BC shall reject the update.
/// - R4.5 — While the caller has valid credentials, when a password update is accepted, the BC shall retain credential verification material and shall not retain the submitted plaintext password. _(why: accepted credentials must not expose reusable secrets at rest)_
/// - R4.6 — If the caller supplies a credential that is not a valid JWT token, then the BC shall reject the update.
/// - R4.7 — While the caller has valid credentials, when an account or profile update is accepted, the BC shall durably retain the changed account, credential verification material, and public profile.
///
/// ### R5: View a profile
/// - R5.1 — When an existing username is requested, the BC shall return that user's public profile.
/// - R5.2 — While the caller has valid credentials, when an existing username is requested, the BC shall include whether the caller follows that user.
/// - R5.3 — If the requested username does not identify an existing user, then the BC shall reject the profile request.
/// - R5.4 — If the caller supplies a credential that is not a valid JWT token, then the BC shall ignore that credential when returning a public profile.
///
/// ### R6: Follow a user
/// - R6.1 — While the caller has valid credentials, when an existing different user is followed, the BC shall record that following relationship and return the followed user's profile.
/// - R6.2 — While the caller already follows the requested user, when that user is followed again, the BC shall leave the relationship unchanged and return the followed user's profile.
/// - R6.3 — If the caller lacks valid credentials, then the BC shall reject the follow request.
/// - R6.4 — If the requested user does not exist or is the caller, then the BC shall reject the follow request.
/// - R6.5 — If the caller supplies a credential that is not a valid JWT token, then the BC shall reject the follow request.
/// - R6.6 — While the caller has valid credentials, when a following relationship is accepted, the BC shall durably retain that following relationship.
///
/// ### R7: Unfollow a user
/// - R7.1 — While the caller has valid credentials, when a followed user is unfollowed, the BC shall remove that following relationship and return the unfollowed user's profile.
/// - R7.2 — While the caller does not follow the requested user, when that user is unfollowed, the BC shall leave relationships unchanged and return the requested user's profile.
/// - R7.3 — If the caller lacks valid credentials, then the BC shall reject the unfollow request.
/// - R7.4 — If the requested user does not exist or is the caller, then the BC shall reject the unfollow request.
/// - R7.5 — If the caller supplies a credential that is not a valid JWT token, then the BC shall reject the unfollow request.
/// - R7.6 — While the caller has valid credentials, when an unfollow request is accepted, the BC shall durably retain the absence of that following relationship.
///
/// ## Entities
/// - User, Profile, PasswordCredential
///
/// ## Out of scope
/// - Article authoring, article feeds, favorites, email delivery, and logout or credential revocation.
package dearrudam.realworld.users;
