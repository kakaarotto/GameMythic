<template>
  <div class="display-keyboard" ref="oninput" @focusout="inputBlur">
    <div class="something-input" ref="oninput_translate">
      <input
        class="ion-input"
        placeholder="Say something..."
        ref="modelinput"
        type="text"
        @input="editorMessage"
        v-model="content"
        @blur.event="changeBlur($event)"
      />
    </div>
    <div class="onSend" ref="onsendComment">
      <ion-button @click="sendComment">Send</ion-button>
    </div>
  </div>
</template>

<script>
import { createData } from '../api/comment';
import { IonInput, IonButton } from '@ionic/vue';

export default {
  name: 'detailsComment',
  components: {
    IonButton,
    IonInput
  },
  props: {
    display: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      displaykeyboard: false,
      content: '',
      autofocus: false,
      timer: 0
    };
  },
  mounted() {
    setTimeout(() => {
      const scrollHeight = document.documentElement.scrollTop || document.body.scrollTop || 0;
      window.scrollTo(0, Math.max(scrollHeight - 1, 0));
    }, 300);
  },
  beforeUpdate() {
    // Check the value of the updated input to determine the style based on the current value
    this.$refs.oninput_translate.className =
      this.content != '' && this.content != null
        ? 'something-input ion_input_width_85'
        : 'something-input';
  },
  methods: {
    inputBlur(e) {
      if (this.timer) {
        clearTimeout(this.timer);
      }
      // Determines whether the target element triggering the event is an input box, focusing only on the behaviour of the input box
      if (e && e.target && e.target.tagName && e.target.tagName.toLowerCase() === 'input') {
        this.timer = setTimeout(() => {
          window.scrollTo(0, 0);
        }, 200);
      }
    },
    editorMessage(event) {
      if (!event.target.value || event.target.value != null) {
        this.$refs.oninput_translate.className = 'something-input ion_input_width_85';
        this.$refs.onsendComment.className = 'onSend is-actives';
      }
      if (event.target.value == '' || event.target.value == null) {
        this.$refs.oninput_translate.className = 'something-input';
        this.$refs.onsendComment.className = 'onSend';
      }
    },
    // Leave comment
    sendComment() {
      let data = {
        goodsId: this.$route.query.contentId,
        content: this.content
      };
      data.userId = JSON.parse(localStorage.getItem('userInfo')).userId;
      createData(data).then((res) => {
        if (res.data.code == 1) {
          this.$refs.oninput.className = 'display-keyboard';
          // Restore the previous style
          this.$refs.oninput_translate.className = 'something-input';
          this.$refs.onsendComment.className = 'onSend';
          // Clear content
          this.content = null;
          this.$refs.modelinput.value == null;
          // Hide this component waiting call it next time
          this.displaykeyboard = false;
          this.$mybus.emit('detailBus', false);
          setTimeout(() => {
            const scrollHeight = document.documentElement.scrollTop || document.body.scrollTop || 0;
            window.scrollTo(0, Math.max(scrollHeight - 1, 0));
          }, 300);
        }
      });
    },
    // ios keyboard recall, footer does not callback
    changeBlur() {
      this.$refs.oninput_translate.className = 'something-input';
      this.content;
      this.displaykeyboard = false;
      this.$mybus.emit('detailBus', false);
      setTimeout(() => {
        const scrollHeight = document.documentElement.scrollTop || document.body.scrollTop || 0;
        window.scrollTo(0, Math.max(scrollHeight - 1, 0));
      }, 100);
    }
  },
  // Listen for data change events
  watch: {
    display: {
      handler(newval, oldval) {
        this.displaykeyboard = newval;
        if (newval == true) {
          this.$refs.oninput.className = 'display-keyboard is-actives';
          this.$refs.modelinput.focus();
        }
        if (newval == false) {
          this.$refs.oninput.className = 'display-keyboard';
          this.autofocus = false;
        }
      }
    }
  }
};
</script>

<style scoped>
/* Evoke comment input style */
.display-keyboard {
  background: #ffffff;
  width: 100%;
  transform: translateX(-650px);
  z-index: 888;
  padding: 21px 20px;
  transition: all 0.5s;
  align-items: center;
  display: flex;
  position: absolute;
  bottom: 0px;
}

.transition_on_keyboard {
  bottom: 0px;
}
.something-input {
  width: 100%;
  flex: none;
  transition: all 0.6s;
}
.ion_input_width_85 {
  width: 82%;
  padding-right: 5px;
}

.onSend {
  display: none;
}
ion-button {
  height: 35px;
  --background: #ff9820 !important;
}
.ion-input {
  background: #f1f1f1;
  color: #000;
  border-radius: 100px;
  width: 100%;
  border: none;
  outline: none;
  padding: 13px 15px;
}
.is-actives {
  display: flex;
  transform: translateX(0px);
}
</style>
<style type="text/css">
.something-input input.native-input {
  padding-left: 11px;
}
</style>