# SimpleLocalize-CLI 

## ⚡️ What it does?

Application will find all keys which are used by [https://github.com/yahoo/react-intl](https://github.com/yahoo/react-intl).
Like:
```jsx
<FormattedMessage id="some_key"/>
```
or 
```js
intl.formatMessage(defineMessages({
  id: "some_key"
});
```
Keys will be pushed to the [SimpleLocalize cloud](https://app.simplelocalize.io/login), where you will be able to manage translations for multiple languages with ease, and publish them to our CDN.

Consider use of [`react-intl-simplelocalize`](https://github.com/simplelocalize/react-intl-simplelocalize) for the best experience. 

## ⚙️ Installation

```bash
$ curl -sL https://github.com/simplelocalize/simplelocalize-cli/releases/download/0.0.0/simplelocalize | bash
```

## 🛠 Configuration

- Create account here: https://simplelocalize.io/register
- Create example project
- Go to project settings and download configuration properties

**Example `simplelocalize.yml`**
```yml
searchDir: /Users/{YOUR_NAME}/Workspace/MyProject/src //OPTIONAL
clientId: <YOUR_CLIENT_ID>
clientSecret: <YOUR_SECRET>
projectToken: <PROJECT_TOKEN>
projectType: <PROJECT_TYPE>
```

## 🚀 Usage

```bash
$ simplelocalize-cli
```
Application will find all i18n keys and push them to SimpleLocalize cloud.

## 💡 Further work

- Support more project types

## 👩‍⚖️ License

MIT © [](https://github.com/)
