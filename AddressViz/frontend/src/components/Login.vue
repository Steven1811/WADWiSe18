<template>
    <v-container fluid fill-height>
        <v-layout align-center justify-center>
            <v-flex xs12 sm8 md4>
                <v-card class="elevation-12">
                    <v-toolbar>
                        <v-toolbar-title>Login</v-toolbar-title>
                    </v-toolbar>
                    <v-form ref="loginForm" v-model="valid" lazy-validation>
                        <v-card-text>
                                <v-text-field 
                                    prepend-icon="person"
                                    v-model="user"
                                    label="User"
                                    type="text"
                                    :rules="userRules"
                                    required
                                ></v-text-field>
                                <v-text-field
                                    id="password"
                                    prepend-icon="lock"
                                    v-model="password"
                                    label="Password"
                                    type="password"
                                    :rules="passwordRules"
                                    required
                                ></v-text-field>
                        </v-card-text>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn
                                :disabled="!valid"
                                @click="submit"
                            >Login</v-btn>
                            <v-btn @click="clear">clear</v-btn>
                        </v-card-actions>
                    </v-form>
                </v-card>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script>
  export default {
    data: () => ({
      valid: false,
      user: '',
      password: '',
      userRules: [
        v => !!v || 'User is required'
      ],
      passwordRules: [
        v => !!v || 'Password is required'
      ]
    }),
    methods: {
        submit () {
            if (this.$refs.loginForm.validate()) {
                this.$router.push({ name: 'Main' })
            }
        },
        clear () {
            this.$refs.loginForm.reset()
        }
    }
  }
</script>