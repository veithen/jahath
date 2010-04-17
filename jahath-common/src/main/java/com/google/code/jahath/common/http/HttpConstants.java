/*
 * Copyright 2009-2010 Andreas Veithen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.jahath.common.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines constants used by HTTP clients and servers.
 * 
 * @author Andreas Veithen
 */
public class HttpConstants {
    private HttpConstants() {}
    
    /**
     * Defines constants for HTTP status codes.
     */
    public static class StatusCodes {
        private static final Map<Integer,String> reasonPhrases = new HashMap<Integer,String>();
        
        private StatusCodes() {}
        
        /**
         * Constant for HTTP status code 100 (Continue). <blockquote>The client SHOULD continue with its
         * request. This interim response is used to inform the client that the initial part of the
         * request has been received and has not yet been rejected by the server. The client SHOULD
         * continue by sending the remainder of the request or, if the request has already been
         * completed, ignore this response. The server MUST send a final response after the request has
         * been completed.</blockquote>
         */
        public static final int CONTINUE = 100;
    
        /**
         * Constant for HTTP status code 101 (Switching Protocols). <blockquote>The server understands
         * and is willing to comply with the client's request, via the <tt>Upgrade</tt> message header
         * field, for a change in the application protocol being used on this connection. The server
         * will switch protocols to those defined by the response's <tt>Upgrade</tt> header field
         * immediately after the empty line which terminates the 101 response.
         * <p>
         * The protocol SHOULD be switched only when it is advantageous to do so. For example, switching
         * to a newer version of HTTP is advantageous over older versions, and switching to a real-time,
         * synchronous protocol might be advantageous when delivering resources that use such
         * features.</blockquote>
         */
        public static final int SWITCHING_PROTOCOLS = 101;
    
        /**
         * Constant for HTTP status code 200 (OK). <blockquote>The request has succeeded. The
         * information returned with the response is dependent on the method used in the request, for
         * example:
         * <p>
         * <tt>GET</tt> an entity corresponding to the requested resource is sent in the response;
         * <p>
         * <tt>HEAD</tt> the entity-header fields corresponding to the requested resource are sent in
         * the response without any message-body;
         * <p>
         * <tt>POST</tt> an entity describing or containing the result of the action;
         * <p>
         * <tt>TRACE</tt> an entity containing the request message as received by the end
         * server.</blockquote>
         */
        public static final int OK = 200;
    
        /**
         * Constant for HTTP status code 201 (Created). <blockquote>The request has been fulfilled and
         * resulted in a new resource being created. The newly created resource can be referenced by the
         * URI(s) returned in the entity of the response, with the most specific URI for the resource
         * given by a <tt>Location</tt> header field. The response SHOULD include an entity containing a
         * list of resource characteristics and location(s) from which the user or user agent can choose
         * the one most appropriate. The entity format is specified by the media type given in the
         * <tt>Content-Type</tt> header field. The origin server MUST create the resource before
         * returning the 201 status code. If the action cannot be carried out immediately, the server
         * SHOULD respond with 202 (Accepted) response instead.</blockquote>
         */
        public static final int CREATED = 201;
    
        /**
         * Constant for HTTP status code 202 (Accepted). <blockquote>The request has been accepted for
         * processing, but the processing has not been completed. The request might or might not
         * eventually be acted upon, as it might be disallowed when processing actually takes place.
         * There is no facility for re-sending a status code from an asynchronous operation such as
         * this.
         * <p>
         * The 202 response is intentionally non-committal. Its purpose is to allow a server to accept a
         * request for some other process (perhaps a batch-oriented process that is only run once per
         * day) without requiring that the user agent's connection to the server persist until the
         * process is completed. The entity returned with this response SHOULD include an indication of
         * the request's current status and either a pointer to a status monitor or some estimate of
         * when the user can expect the request to be fulfilled.</blockquote>
         */
        public static final int ACCEPTED = 202;
    
        /**
         * Constant for HTTP status code 203 (Non-Authoritative Information). <blockquote>The returned
         * metainformation in the entity-header is not the definitive set as available from the origin
         * server, but is gathered from a local or a third-party copy. The set presented MAY be a subset
         * or superset of the original version. For example, including local annotation information
         * about the resource might result in a superset of the metainformation known by the origin
         * server. Use of this response code is not required and is only appropriate when the response
         * would otherwise be 200 (OK).</blockquote>
         */
        public static final int NON_AUTHORITATIVE_INFORMATION = 203;
    
        /**
         * Constant for HTTP status code 204 (No Content). <blockquote>The server has fulfilled the
         * request but does not need to return an entity-body, and might want to return updated
         * metainformation. The response MAY include new or updated metainformation in the form of
         * entity-headers, which if present SHOULD be associated with the requested variant.
         * <p>
         * If the client is a user agent, it SHOULD NOT change its document view from that which caused
         * the request to be sent. This response is primarily intended to allow input for actions to
         * take place without causing a change to the user agent's active document view, although any
         * new or updated metainformation SHOULD be applied to the document currently in the user
         * agent's active view.
         * <p>
         * The 204 response MUST NOT include a message-body, and thus is always terminated by the first
         * empty line after the header fields.</blockquote>
         */
        public static final int NO_CONTENT = 204;
    
        /**
         * Constant for HTTP status code 205 (Reset Content). <blockquote>The server has fulfilled the
         * request and the user agent SHOULD reset the document view which caused the request to be
         * sent. This response is primarily intended to allow input for actions to take place via user
         * input, followed by a clearing of the form in which the input is given so that the user can
         * easily initiate another input action. The response MUST NOT include an entity.</blockquote>
         */
        public static final int RESET_CONTENT = 205;
    
        /**
         * Constant for HTTP status code 206 (Partial Content). <blockquote>The server has fulfilled the
         * partial <tt>GET</tt> request for the resource. The request MUST have included a
         * <tt>Range</tt> header field indicating the desired range, and MAY have included an
         * <tt>If-Range</tt> header field to make the request conditional.
         * <p>
         * The response MUST include the following header fields:
         * <ul>
         * <li>Either a <tt>Content-Range</tt> header field indicating the range included with this
         * response, or a <tt>multipart/byteranges</tt> <tt>Content-Type</tt> including
         * <tt>Content-Range</tt> fields for each part. If a <tt>Content-Length</tt> header field is
         * present in the response, its value MUST match the actual number of OCTETs transmitted in the
         * message-body.
         * <li><tt>Date</tt>
         * <li><tt>ETag</tt> and/or <tt>Content-Location</tt>, if the header would have been sent in a
         * 200 response to the same request
         * <li><tt>Expires</tt>, <tt>Cache-Control</tt>, and/or <tt>Vary</tt>, if the field-value might
         * differ from that sent in any previous response for the same variant
         * </ul>
         * If the 206 response is the result of an If-Range request that used a strong cache validator,
         * the response SHOULD NOT include other entity-headers. If the response is the result of an
         * <tt>If-Range</tt> request that used a weak validator, the response MUST NOT include other
         * entity-headers; this prevents inconsistencies between cached entity-bodies and updated
         * headers. Otherwise, the response MUST include all of the entity-headers that would have been
         * returned with a 200 (OK) response to the same request.
         * <p>
         * A cache MUST NOT combine a 206 response with other previously cached content if the
         * <tt>ETag</tt> or <tt>Last-Modified</tt> headers do not match exactly.
         * <p>
         * A cache that does not support the <tt>Range</tt> and <tt>Content-Range</tt> headers MUST NOT
         * cache 206 (Partial) responses.</blockquote>
         */
        public static final int PARTIAL_CONTENT = 206;
    
        /**
         * Constant for HTTP status code 300 (Multiple Choices). <blockquote>The requested resource
         * corresponds to any one of a set of representations, each with its own specific location, and
         * agent-driven negotiation information is being provided so that the user (or user agent) can
         * select a preferred representation and redirect its request to that location.
         * <p>
         * Unless it was a <tt>HEAD</tt> request, the response SHOULD include an entity containing a
         * list of resource characteristics and location(s) from which the user or user agent can choose
         * the one most appropriate. The entity format is specified by the media type given in the
         * <tt>Content-Type</tt> header field. Depending upon the format and the capabilities of the
         * user agent, selection of the most appropriate choice MAY be performed automatically. However,
         * this specification does not define any standard for such automatic selection.
         * <p>
         * If the server has a preferred choice of representation, it SHOULD include the specific URI
         * for that representation in the <tt>Location</tt> field; user agents MAY use the Location
         * field value for automatic redirection. This response is cacheable unless indicated
         * otherwise.</blockquote>
         */
        public static final int MULTIPLE_CHOICES = 300;
    
        /**
         * Constant for HTTP status code 301 (Moved Permanently). <blockquote>The requested resource has
         * been assigned a new permanent URI and any future references to this resource SHOULD use one
         * of the returned URIs. Clients with link editing capabilities ought to automatically re-link
         * references to the Request-URI to one or more of the new references returned by the server,
         * where possible. This response is cacheable unless indicated otherwise.
         * <p>
         * The new permanent URI SHOULD be given by the <tt>Location</tt> field in the response. Unless
         * the request method was <tt>HEAD</tt>, the entity of the response SHOULD contain a short
         * hypertext note with a hyperlink to the new URI(s).
         * <p>
         * If the 301 status code is received in response to a request other than <tt>GET</tt> or
         * <tt>HEAD</tt>, the user agent MUST NOT automatically redirect the request unless it can be
         * confirmed by the user, since this might change the conditions under which the request was
         * issued.</blockquote>
         */
        public static final int MOVED_PERMANENTLY = 301;
    
        /**
         * Constant for HTTP status code 302 (Found). <blockquote>The requested resource resides
         * temporarily under a different URI. Since the redirection might be altered on occasion, the
         * client SHOULD continue to use the Request-URI for future requests. This response is only
         * cacheable if indicated by a <tt>Cache-Control</tt> or <tt>Expires</tt> header field.
         * <p>
         * The temporary URI SHOULD be given by the <tt>Location</tt> field in the response. Unless the
         * request method was <tt>HEAD</tt>, the entity of the response SHOULD contain a short hypertext
         * note with a hyperlink to the new URI(s).
         * <p>
         * If the 302 status code is received in response to a request other than <tt>GET</tt> or
         * <tt>HEAD</tt>, the user agent MUST NOT automatically redirect the request unless it can be
         * confirmed by the user, since this might change the conditions under which the request was
         * issued.
         * <p>
         * Note: RFC 1945 and RFC 2068 specify that the client is not allowed to change the method on
         * the redirected request. However, most existing user agent implementations treat 302 as if it
         * were a 303 response, performing a <tt>GET</tt> on the <tt>Location</tt> field-value
         * regardless of the original request method. The status codes 303 and 307 have been added for
         * servers that wish to make unambiguously clear which kind of reaction is expected of the
         * client. </blockquote>
         */
        public static final int FOUND = 302;
    
        /**
         * Constant for HTTP status code 303 (See Other). <blockquote>The response to the request can be
         * found under a different URI and SHOULD be retrieved using a <tt>GET</tt> method on that
         * resource. This method exists primarily to allow the output of a POST-activated script to
         * redirect the user agent to a selected resource. The new URI is not a substitute reference for
         * the originally requested resource. The 303 response MUST NOT be cached, but the response to
         * the second (redirected) request might be cacheable.
         * <p>
         * The different URI SHOULD be given by the <tt>Location</tt> field in the response. Unless the
         * request method was <tt>HEAD</tt>, the entity of the response SHOULD contain a short hypertext
         * note with a hyperlink to the new URI(s).
         * <p>
         * Note: Many pre-HTTP/1.1 user agents do not understand the 303 status. When interoperability
         * with such clients is a concern, the 302 status code may be used instead, since most user
         * agents react to a 302 response as described here for 303.</blockquote>
         */
        public static final int SEE_OTHER = 303;
    
        /**
         * Constant for HTTP status code 304 (Not Modified). <blockquote>If the client has performed a
         * conditional <tt>GET</tt> request and access is allowed, but the document has not been
         * modified, the server SHOULD respond with this status code. The 304 response MUST NOT contain
         * a message-body, and thus is always terminated by the first empty line after the header
         * fields.
         * <p>
         * The response MUST include the following header fields:
         * <ul>
         * <li><tt>Date</tt>, unless its omission is required.
         * <p>
         * If a clockless origin server obeys these rules, and proxies and clients add their own
         * <tt>Date</tt> to any response received without one, caches will operate correctly.
         * <li><tt>ETag</tt> and/or <tt>Content-Location</tt>, if the header would have been sent in a
         * 200 response to the same request
         * <li><tt>Expires</tt>, <tt>Cache-Control</tt>, and/or <tt>Vary</tt>, if the field-value might
         * differ from that sent in any previous response for the same variant
         * </ul>
         * If the conditional <tt>GET</tt> used a strong cache validator (see section 13.3.3), the
         * response SHOULD NOT include other entity-headers. Otherwise (i.e., the conditional
         * <tt>GET</tt> used a weak validator), the response MUST NOT include other entity-headers; this
         * prevents inconsistencies between cached entity-bodies and updated headers.
         * <p>
         * If a 304 response indicates an entity not currently cached, then the cache MUST disregard the
         * response and repeat the request without the conditional.
         * <p>
         * If a cache uses a received 304 response to update a cache entry, the cache MUST update the
         * entry to reflect any new field values given in the response.</blockquote>
         */
        public static final int NOT_MODIFIED = 304;
    
        /**
         * Constant for HTTP status code 305 (Use Proxy). <blockquote>The requested resource MUST be
         * accessed through the proxy given by the <tt>Location</tt> field. The <tt>Location</tt> field gives the URI of
         * the proxy. The recipient is expected to repeat this single request via the proxy. 305
         * responses MUST only be generated by origin servers.
         * <p>
         * Note: RFC 2068 was not clear that 305 was intended to redirect a single request, and to be
         * generated by origin servers only. Not observing these limitations has significant security
         * consequences.</blockquote>
         */
        public static final int USE_PROXY = 305;
    
        /**
         * Constant for HTTP status code 307 (Temporary Redirect). <blockquote>The requested resource
         * resides temporarily under a different URI. Since the redirection MAY be altered on occasion,
         * the client SHOULD continue to use the Request-URI for future requests. This response is only
         * cacheable if indicated by a <tt>Cache-Control</tt> or <tt>Expires</tt> header field.
         * <p>
         * The temporary URI SHOULD be given by the <tt>Location</tt> field in the response. Unless the
         * request method was <tt>HEAD</tt>, the entity of the response SHOULD contain a short hypertext
         * note with a hyperlink to the new URI(s) , since many pre-HTTP/1.1 user agents do not
         * understand the 307 status. Therefore, the note SHOULD contain the information necessary for a
         * user to repeat the original request on the new URI.
         * <p>
         * If the 307 status code is received in response to a request other than <tt>GET</tt> or
         * <tt>HEAD</tt>, the user agent MUST NOT automatically redirect the request unless it can be
         * confirmed by the user, since this might change the conditions under which the request was
         * issued.</blockquote>
         */
        public static final int TEMPORARY_REDIRECT = 307;
    
        /**
         * Constant for HTTP status code 400 (Bad Request). <blockquote>The request could not be
         * understood by the server due to malformed syntax. The client SHOULD NOT repeat the request
         * without modifications.</blockquote>
         */
        public static final int BAD_REQUEST = 400;
    
        /**
         * Constant for HTTP status code 401 (Unauthorized). <blockquote>The request requires user
         * authentication. The response MUST include a <tt>WWW-Authenticate</tt> header field containing
         * a challenge applicable to the requested resource. The client MAY repeat the request with a
         * suitable <tt>Authorization</tt> header field (section 14.8). If the request already included
         * <tt>Authorization</tt> credentials, then the 401 response indicates that authorization has
         * been refused for those credentials. If the 401 response contains the same challenge as the
         * prior response, and the user agent has already attempted authentication at least once, then
         * the user SHOULD be presented the entity that was given in the response, since that entity
         * might include relevant diagnostic information.</blockquote>
         */
        public static final int UNAUTHORIZED = 401;
    
        /**
         * Constant for HTTP status code 403 (Forbidden). <blockquote>The server understood the request,
         * but is refusing to fulfill it. Authorization will not help and the request SHOULD NOT be
         * repeated. If the request method was not <tt>HEAD</tt> and the server wishes to make public
         * why the request has not been fulfilled, it SHOULD describe the reason for the refusal in the
         * entity. If the server does not wish to make this information available to the client, the
         * status code 404 (Not Found) can be used instead.</blockquote>
         */
        public static final int FORBIDDEN = 403;
    
        /**
         * Constant for HTTP status code 404 (Not Found). <blockquote>The server has not found anything
         * matching the Request-URI. No indication is given of whether the condition is temporary or
         * permanent. The 410 (Gone) status code SHOULD be used if the server knows, through some
         * internally configurable mechanism, that an old resource is permanently unavailable and has no
         * forwarding address. This status code is commonly used when the server does not wish to reveal
         * exactly why the request has been refused, or when no other response is
         * applicable.</blockquote>
         */
        public static final int NOT_FOUND = 404;
    
        /**
         * Constant for HTTP status code 405 (Method Not Allowed). <blockquote>The method specified in
         * the Request-Line is not allowed for the resource identified by the Request-URI. The response
         * MUST include an <tt>Allow</tt> header containing a list of valid methods for the requested
         * resource.</blockquote>
         */
        public static final int METHOD_NOT_ALLOWED = 405;
    
        /**
         * Constant for HTTP status code 406 (Not Acceptable). <blockquote>The resource identified by
         * the request is only capable of generating response entities which have content
         * characteristics not acceptable according to the accept headers sent in the request.
         * <p>
         * Unless it was a <tt>HEAD</tt> request, the response SHOULD include an entity containing a
         * list of available entity characteristics and location(s) from which the user or user agent
         * can choose the one most appropriate. The entity format is specified by the media type given
         * in the <tt>Content-Type</tt> header field. Depending upon the format and the capabilities of
         * the user agent, selection of the most appropriate choice MAY be performed automatically.
         * However, this specification does not define any standard for such automatic selection.
         * <p>
         * Note: HTTP/1.1 servers are allowed to return responses which are not acceptable according to
         * the accept headers sent in the request. In some cases, this may even be preferable to sending
         * a 406 response. User agents are encouraged to inspect the headers of an incoming response to
         * determine if it is acceptable.
         * <p>
         * If the response could be unacceptable, a user agent SHOULD temporarily stop receipt of more
         * data and query the user for a decision on further actions.</blockquote>
         */
        public static final int NOT_ACCEPTABLE = 406;
    
        /**
         * Constant for HTTP status code 407 (Proxy Authentication Required). <blockquote>This code is
         * similar to 401 (Unauthorized), but indicates that the client must first authenticate itself
         * with the proxy. The proxy MUST return a <tt>Proxy-Authenticate</tt> header field containing a
         * challenge applicable to the proxy for the requested resource. The client MAY repeat the
         * request with a suitable <tt>Proxy-Authorization</tt> header field.</blockquote>
         */
        public static final int PROXY_AUTHENTICATION_REQUIRED = 407;
    
        /**
         * Constant for HTTP status code 408 (Request Timeout). <blockquote>The client did not produce a
         * request within the time that the server was prepared to wait. The client MAY repeat the
         * request without modifications at any later time.</blockquote>
         */
        public static final int REQUEST_TIMEOUT = 408;
    
        /**
         * Constant for HTTP status code 409 (Conflict). <blockquote>The request could not be completed
         * due to a conflict with the current state of the resource. This code is only allowed in
         * situations where it is expected that the user might be able to resolve the conflict and
         * resubmit the request. The response body SHOULD include enough information for the user to
         * recognize the source of the conflict. Ideally, the response entity would include enough
         * information for the user or user agent to fix the problem; however, that might not be
         * possible and is not required.
         * <p>
         * Conflicts are most likely to occur in response to a <tt>PUT</tt> request. For example, if
         * versioning were being used and the entity being <tt>PUT</tt> included changes to a resource
         * which conflict with those made by an earlier (third-party) request, the server might use the
         * 409 response to indicate that it can't complete the request. In this case, the response
         * entity would likely contain a list of the differences between the two versions in a format
         * defined by the response <tt>Content-Type</tt>.</blockquote>
         */
        public static final int CONFLICT = 409;
    
        /**
         * Constant for HTTP status code 410 (Gone). <blockquote>The requested resource is no longer
         * available at the server and no forwarding address is known. This condition is expected to be
         * considered permanent. Clients with link editing capabilities SHOULD delete references to the
         * Request-URI after user approval. If the server does not know, or has no facility to
         * determine, whether or not the condition is permanent, the status code 404 (Not Found) SHOULD
         * be used instead. This response is cacheable unless indicated otherwise.
         * <p>
         * The 410 response is primarily intended to assist the task of web maintenance by notifying the
         * recipient that the resource is intentionally unavailable and that the server owners desire
         * that remote links to that resource be removed. Such an event is common for limited-time,
         * promotional services and for resources belonging to individuals no longer working at the
         * server's site. It is not necessary to mark all permanently unavailable resources as "gone" or
         * to keep the mark for any length of time -- that is left to the discretion of the server
         * owner.</blockquote>
         */
        public static final int GONE = 410;
    
        /**
         * Constant for HTTP status code 411 (Length Required). <blockquote>The server refuses to accept
         * the request without a defined <tt>Content-Length</tt>. The client MAY repeat the request if
         * it adds a valid <tt>Content-Length</tt> header field containing the length of the
         * message-body in the request message.</blockquote>
         */
        public static final int LENGTH_REQUIRED = 411;
    
        /**
         * Constant for HTTP status code 412 (Precondition Failed). <blockquote>The precondition given
         * in one or more of the request-header fields evaluated to false when it was tested on the
         * server. This response code allows the client to place preconditions on the current resource
         * metainformation (header field data) and thus prevent the requested method from being applied
         * to a resource other than the one intended.</blockquote>
         */
        public static final int PRECONDITION_FAILED = 412;
    
        /**
         * Constant for HTTP status code 413 (Request Entity Too Large). <blockquote>The server is
         * refusing to process a request because the request entity is larger than the server is willing
         * or able to process. The server MAY close the connection to prevent the client from continuing
         * the request.
         * <p>
         * If the condition is temporary, the server SHOULD include a <tt>Retry-After</tt> header field
         * to indicate that it is temporary and after what time the client MAY try again.</blockquote>
         */
        public static final int REQUEST_ENTITY_TOO_LARGE = 413;
    
        /**
         * Constant for HTTP status code 414 (Request-URI Too Long). <blockquote>The server is refusing
         * to service the request because the Request-URI is longer than the server is willing to
         * interpret. This rare condition is only likely to occur when a client has improperly converted
         * a POST request to a GET request with long query information, when the client has descended
         * into a URI "black hole" of redirection (e.g., a redirected URI prefix that points to a suffix
         * of itself), or when the server is under attack by a client attempting to exploit security
         * holes present in some servers using fixed-length buffers for reading or manipulating the
         * Request-URI.</blockquote>
         */
        public static final int REQUEST_URI_TOO_LONG = 414;
    
        /**
         * Constant for HTTP status code 415 (Unsupported Media Type). <blockquote>The server is
         * refusing to service the request because the entity of the request is in a format not
         * supported by the requested resource for the requested method.</blockquote>
         */
        public static final int UNSUPPORTED_MEDIA_TYPE = 415;
    
        /**
         * Constant for HTTP status code 416 (Requested Range Not Satisfiable). <blockquote>A server
         * SHOULD return a response with this status code if a request included a <tt>Range</tt>
         * request-header field, and none of the range-specifier values in this field overlap the
         * current extent of the selected resource, and the request did not include an <tt>If-Range</tt>
         * request-header field. (For byte-ranges, this means that the first-byte-pos of all of the
         * byte-range-spec values were greater than the current length of the selected resource.)
         * <p>
         * When this status code is returned for a byte-range request, the response SHOULD include a
         * <tt>Content-Range</tt> entity-header field specifying the current length of the selected
         * resource. This response MUST NOT use the <tt>multipart/byteranges</tt>
         * content-type.</blockquote>
         */
        public static final int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    
        /**
         * Constant for HTTP status code 417 (Expectation Failed). <blockquote>The expectation given in
         * an <tt>Expect</tt> request-header field could not be met by this server, or, if the server is
         * a proxy, the server has unambiguous evidence that the request could not be met by the
         * next-hop server.</blockquote>
         */
        public static final int EXPECTATION_FAILED = 417;
    
        /**
         * Constant for HTTP status code 500 (Internal Server Error). <blockquote>The server encountered
         * an unexpected condition which prevented it from fulfilling the request.</blockquote>
         */
        public static final int INTERNAL_SERVER_ERROR = 500;
    
        /**
         * Constant for HTTP status code 501 (Not Implemented). <blockquote>The server does not support
         * the functionality required to fulfill the request. This is the appropriate response when the
         * server does not recognize the request method and is not capable of supporting it for any
         * resource.</blockquote>
         */
        public static final int NOT_IMPLEMENTED = 501;
    
        /**
         * Constant for HTTP status code 502 (Bad Gateway). <blockquote>The server, while acting as a
         * gateway or proxy, received an invalid response from the upstream server it accessed in
         * attempting to fulfill the request.</blockquote>
         */
        public static final int BAD_GATEWAY = 502;
    
        /**
         * Constant for HTTP status code 503 (Service Unavailable). <blockquote>The server is currently
         * unable to handle the request due to a temporary overloading or maintenance of the server. The
         * implication is that this is a temporary condition which will be alleviated after some delay.
         * If known, the length of the delay MAY be indicated in a <tt>Retry-After</tt> header. If no
         * <tt>Retry-After</tt> is given, the client SHOULD handle the response as it would for a 500
         * response.
         * <p>
         * Note: The existence of the 503 status code does not imply that a server must use it when
         * becoming overloaded. Some servers may wish to simply refuse the connection.</blockquote>
         */
        public static final int SERVICE_UNAVAILABLE = 503;
    
        /**
         * Constant for HTTP status code 504 (Gateway Timeout). <blockquote>The server, while acting as
         * a gateway or proxy, did not receive a timely response from the upstream server specified by
         * the URI (e.g. HTTP, FTP, LDAP) or some other auxiliary server (e.g. DNS) it needed to access
         * in attempting to complete the request.
         * <p>
         * Note: Note to implementors: some deployed proxies are known to return 400 or 500 when DNS
         * lookups time out.</blockquote>
         */
        public static final int GATEWAY_TIMEOUT = 504;
    
        /**
         * Constant for HTTP status code 505 (HTTP Version Not Supported). <blockquote>The server does
         * not support, or refuses to support, the HTTP protocol version that was used in the request
         * message. The server is indicating that it is unable or unwilling to complete the request
         * using the same major version as the client, as described in section 3.1, other than with this
         * error message. The response SHOULD contain an entity describing why that version is not
         * supported and what other protocols are supported by that server.</blockquote>
         */
        public static final int HTTP_VERSION_NOT_SUPPORTED = 505;

        /**
         * Get the reason phrase for a given status code.
         * 
         * @param code the status code
         * @return the corresponding reason phrase
         * @throws IllegalArgumentException if the status code is unknown
         */
        public static String getReasonPhrase(int code) {
            String reasonPhrase = reasonPhrases.get(code);
            if (reasonPhrase == null) {
                throw new IllegalArgumentException("Unknown status code");
            } else {
                return reasonPhrase;
            }
        }
        
        static {
            reasonPhrases.put(CONTINUE, "Continue");
            reasonPhrases.put(SWITCHING_PROTOCOLS, "Switching Protocols");
            reasonPhrases.put(OK, "OK");
            reasonPhrases.put(CREATED, "Created");
            reasonPhrases.put(ACCEPTED, "Accepted");
            reasonPhrases.put(NON_AUTHORITATIVE_INFORMATION, "Non-Authoritative Information");
            reasonPhrases.put(NO_CONTENT, "No Content");
            reasonPhrases.put(RESET_CONTENT, "Reset Content");
            reasonPhrases.put(PARTIAL_CONTENT, "Partial Content");
            reasonPhrases.put(MULTIPLE_CHOICES, "Multiple Choices");
            reasonPhrases.put(MOVED_PERMANENTLY, "Moved Permanently");
            reasonPhrases.put(FOUND, "Found");
            reasonPhrases.put(SEE_OTHER, "See Other");
            reasonPhrases.put(NOT_MODIFIED, "Not Modified");
            reasonPhrases.put(USE_PROXY, "Use Proxy");
            reasonPhrases.put(TEMPORARY_REDIRECT, "Temporary Redirect");
            reasonPhrases.put(BAD_REQUEST, "Bad Request");
            reasonPhrases.put(UNAUTHORIZED, "Unauthorized");
            reasonPhrases.put(FORBIDDEN, "Forbidden");
            reasonPhrases.put(NOT_FOUND, "Not Found");
            reasonPhrases.put(METHOD_NOT_ALLOWED, "Method Not Allowed");
            reasonPhrases.put(NOT_ACCEPTABLE, "Not Acceptable");
            reasonPhrases.put(PROXY_AUTHENTICATION_REQUIRED, "Proxy Authentication Required");
            reasonPhrases.put(REQUEST_TIMEOUT, "Request Time-out");
            reasonPhrases.put(CONFLICT, "Conflict");
            reasonPhrases.put(GONE, "Gone");
            reasonPhrases.put(LENGTH_REQUIRED, "Length Required");
            reasonPhrases.put(PRECONDITION_FAILED, "Precondition Failed");
            reasonPhrases.put(REQUEST_ENTITY_TOO_LARGE, "Request Entity Too Large");
            reasonPhrases.put(REQUEST_URI_TOO_LONG, "Request-URI Too Long");
            reasonPhrases.put(UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
            reasonPhrases.put(REQUESTED_RANGE_NOT_SATISFIABLE, "Requested range not satisfiable");
            reasonPhrases.put(EXPECTATION_FAILED, "Expectation Failed");
            reasonPhrases.put(INTERNAL_SERVER_ERROR, "Internal Server Error");
            reasonPhrases.put(NOT_IMPLEMENTED, "Not Implemented");
            reasonPhrases.put(BAD_GATEWAY, "Bad Gateway");
            reasonPhrases.put(SERVICE_UNAVAILABLE, "Service Unavailable");
            reasonPhrases.put(GATEWAY_TIMEOUT, "Gateway Time-out");
            reasonPhrases.put(HTTP_VERSION_NOT_SUPPORTED, "HTTP Version not supported");        
        }
    }

    /**
     * Defines constants for header names defined by the HTTP specification.
     */
    public static class Headers {
        private Headers() {}

        /**
         * Constant for the <tt>Connection</tt> header.
         */
        public static final String CONNECTION = "Connection";
        
        /**
         * Constant for the <tt>Content-Length</tt> header.
         */
        public static final String CONTENT_LENGTH = "Content-Length";
    
        /**
         * Constant for the <tt>Content-Type</tt> header.
         */
        public static final String CONTENT_TYPE = "Content-Type";
    
        /**
         * Constant for the <tt>Host</tt> header.
         */
        public static final String HOST = "Host";
        
        /**
         * Constant for the <tt>Location</tt> header.
         */
        public static final String LOCATION = "Location";
        
        /**
         * Constant for the <tt>Proxy-Connection</tt> header.
         */
        public static final String PROXY_CONNECTION = "Proxy-Connection";
        
        /**
         * Constant for the <tt>Transfer-Encoding</tt> header.
         */
        public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    }
    
    public static final String HTTP_VERSION_1_1 = "HTTP/1.1";
}
