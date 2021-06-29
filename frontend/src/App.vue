<template>
  <div class="container">
    <div id="xterm"></div>
  </div>
  <!-- <img alt="Vue logo" src="./assets/logo.png" />
  <HelloWorld msg="Hello Vue 3 + Vite" /> -->
</template>

<script>
import 'xterm/css/xterm.css'
import { Terminal } from 'xterm';
import { FitAddon } from 'xterm-addon-fit';
import LocalEchoController from 'local-echo';
export default {
  name: 'HelloWorld',
  mounted() {
    const redANSI = '\x1b[1;31m';
    const whiteANSI = '\x1b[37m';
    const term = new Terminal({
      rendererType: 'canvas', // 渲染类型
      rows: 40, // 行数
      convertEol: true, // 启用时，光标将设置为下一行的开头
      scrollback: 10, // 终端中的回滚量
      disableStdin: false, // 是否应禁用输入。
      cursorStyle: 'underline', // 光标样式
      cursorBlink: true, // 光标闪烁
      theme: {
        foreground: 'yellow', // 字体
        background: '#060101', // 背景色
        cursor: 'help' // 设置光标
      }
    });
    term.open(document.getElementById('xterm'));
    const fitAddon = new FitAddon();
    term.loadAddon(fitAddon);
    fitAddon.fit();
    term.focus();

    const localEcho = new LocalEchoController(term);
    const f = () => {
      localEcho.read("mua> ")
        .then(() => {
          setTimeout(() => {localEcho.println(`${redANSI}User:${whiteANSI}my name`);;f()}, 10);
          // f()
        })
        .catch(() => console.log('error'))
    }
    f();
  }
}
</script>

<style>
body {
  margin: 0;
}
/* #app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
} */
</style>
