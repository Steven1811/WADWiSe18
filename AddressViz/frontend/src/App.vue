<template>
  <v-app id="inspire" dark>
    <v-toolbar app fixed clipped-left>
      <v-toolbar-title>AddressViz</v-toolbar-title>
    </v-toolbar>
    
    <v-content>
      <router-view></router-view>
    </v-content>

    <v-footer app fixed>
      <v-card
        flat
        tile
        color="grey darken-4"
        class="flex"
      >
        <v-card-text class="text-xs-center">
          <strong>Webentwicklung HTW Berlin</strong><br>
          Steven Bradley, Lincoln
        </v-card-text>
      </v-card>
    </v-footer>
  </v-app>
</template>

<script>
export default {
  name: 'App',
  components: {
    //
  },
  data () {
    return {
      //
    }
  }
}
</script>
