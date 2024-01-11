# Java + JavaScript for Markdown Parsing

This repository is a sample on how to use [unified.js](https://github.com/unifiedjs/unified) for parsing markdown from Java. It uses GraalVM's [Polyglot API](https://www.graalvm.org/latest/reference-manual/polyglot-programming/) to run JavaScript code from Java.

## Goal

The goal of this repository is to show that it's possible as one way to parse markdown in a unified way across frontend and backend using JavaScript.

The following table shows the distinctive possibilities on how to achieve this, where personally I prefer the way it is done in this repository (if markdown parsing has to happen in the backend):

<table>
  <tr>
    <th>Where</th>
    <th>Pro</th>
    <th>Contra</th>
  </tr>
  <tr>
    <td>Backend (Java)</td>
    <td>
      <ol>
        <li>Efficient, because parsing only happens once</li>
        <li>Self-contained since everything runs in the JVM</li>
      </ol>
    </td>
    <td>
      <ol>
        <li>
          Interoperability with Markdown Editor?
          <ul>
            <li>
              Double the work of implementing plugins (e.g. accessibility)
            </li>
            <li>
              Markdown Editor needs to send requests to the backend to 
              display preview
            </li>
          </ul>
        </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td>Delivery-API (Java)</td>
    <td>
      <ol>
        <li>
          Parsing on-demand, so only what actually needs to be parsed gets parsed.
        </li>
        <li>Self-contained since everything runs in the JVM</li>
      </ol>
    </td>
    <td>
      <ol>
        <li>
          Interoperability with Markdown Editor?
          <ul>
            <li>
              Double the work of implementing plugins (e.g. accessibility)
            </li>
            <li>
              Markdown Editor needs to send requests to the backend to 
              display preview
            </li>
          </ul>
        </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td>Backend (Node)</td>
    <td>
      <ol>
        <li>
          It's irrelevant whether it's called from NextJS, the frontend or the backend.
        </li>
        <li>
          Uses JavaScript, which means easy one time plugin development
        </li>
      </ol>
    </td>
    <td>
      <ol>
        <li>
          Separate Node process necessary
          <ul>
            <li>Docker-Container im selben Deployment</li>
            <li>
              <a href="https://blog.termian.dev/posts/nodejs-in-java/">
                Integration into JAR/WAR file
              </a> (Invalidates Pro 1)
            </li>
            <li>
              OpenWHISK Service
            </li>
          </ul>
          Also adds an additional point of failure for where an attacker may introduce malicious code.
        </li>
      </ol>
    </td>
  </tr>
  <tr>
    <td>Backend (Node on Graal)</td>
    <td>
      <ol>
        <li>Efficient, because parsing only happens once</li>
        <li>Self-contained since everything runs in the JVM</li>
        <li>
          Uses JavaScript, which means easy one time development of plugins
        </li>
      </ol>
    </td>
    <td>
      <ol>
        <li>Needs GraalVM, which needs <a href="https://www.oracle.com/downloads/graalvm-downloads.html">a specific version of JDK from Oracle</a>, which means a subscription to Oracle Java SE</li>
      </ol>
    </td>
  </tr>
  <tr>
    <td>NextJS (Server-Side/Static)</td>
    <td>
      <ol>
        <li>
          Exclusively storing Markdown in the Backend:
          <ul>
            <li>Fewer attack vectors for introducing malicious code</li>
            <li>Smaller storage footprint in the backend</li>
            <li>Smaller computational cost on the backend</li>
          </ul>
        </li>
        <li>
          Uses JavaScript, which means easy one time development of plugins
        </li>
      </ol>
    </td>
    <td>
      <ol>
        <li>
          Optimization necessary to reduce the frequency of markdown parsing:
          <ul>
            <li>Possibly already cachable using NextJS</li>
            <li>Manually saving parsed markdown (also needs to be invalidated manually)</li>
          </ul>
        </li>
        <li>Compile time for websites increases</li>
      </ol>
    </td>
  </tr>
</table>

## Setup

You need to be able to run the Java code using a GraalVM java executable and included JDK. This JDK is what provides the additional classes to run multiple languages using Truffle (the multilanguage framework).

The installation the GraalVM also has to include graal.js and graal.node in order to run the JavaScript code.

Steps:
- Install GraalVM with graal.js and graal.node (There's a VSCode Extension for easy management)
- Use the npm binary that is included in the GraalVM bin folder to run `npm install` in the src_js folder.
- Run `npm run build` in the src_js folder

Voilá, now you're able to start the Java program, which should spit out formatted HTML.