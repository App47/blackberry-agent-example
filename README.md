# App47 Blackberry Agent Example App

This is an example Blackberry App that uses [App47](http://app47.com)'s [Blackberry Agent](https://github.com/App47/blackberry-agent). 

## Explanation

This example app demonstrates a working App47 monitored Blackberry app -- with it, you can see a number of features in action including:
  
  * Configuration/Configuration groups
  * Logging
  * Events -- both timed and generic

Additionally, you can use this app to see various meta data related to a particular app, including service endpoints, logging levels, device enablement, etc. 

The [agent code](https://github.com/App47/blackberry-agent) itself isn't included -- you must build that yourself and configure it w/your appropriate App ID. Nevertheless, to configure a Blackberry App to use App47's agent, you should initialize an instance of EmbeddedAgent like so:

`EmbeddedAgent.configureAgent();`

You can see this call in the `App47Screen`'s constructor. Next, for session data, you'll need to hook into an app's lifecycle; thus, if you override the basic Blackberry app lifecycle methods `activate` and `deactivate` you can accordingly hook in session events like so:


    public void deactivate() {
	    EmbeddedAgent.onPause();
	    super.deactivate();
    }

    public void activate() {
	    EmbeddedAgent.onResume();
   	    super.activate();
    }
 


# License

The MIT License

Copyright (c) 2012 App47, Inc.

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE