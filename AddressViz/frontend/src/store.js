import Vue from 'vue'
import Vuex from 'vuex'
import VuexPersist from 'vuex-persist'

const vuexPersist = new VuexPersist({
  key: 'AddressViz',
  storage: localStorage
})


Vue.use(Vuex)

/**
 * contactPayload: {
 *  id: The ContacId to be updated
 *  data: The new Contact data
 * }
 */

export default new Vuex.Store({
  plugins: [vuexPersist.plugin],
  state: {
    contacts: [],
    firstStart: false
  },
  mutations: {
    setFirstStartState(state, isStarted) {
      state.firstStart = isStarted
    },
    addNewContact (state, contact) {
      //Get new ID
      let newId = 5
      if(state.contacts.length > 0) {
        newId = state.contacts.reduce((prev, current) => {
          return (prev.id > current.id) ? prev : current
        }).id + 1
      }

      //Assign
      contact.id = newId
      state.contacts.push(contact)
    },
    deleteContactById (state, contactId) {
      state.contacts = state.contacts.filter((item) => {
          return item.id != contactId;
      });
    },
    updateContactById (state, contactPayload) {
      state.contacts.forEach((item) => {
        if(item.id === contactPayload.id) {
          item.name = contactPayload.data.name
          item.address = contactPayload.data.address
          item.postCode = contactPayload.data.postCode
          item.city = contactPayload.data.city
          item.country = contactPayload.data.country
          item.private = contactPayload.data.private
          item.longitude = contactPayload.data.longitude
          item.latitude = contactPayload.data.latitude
        }
      })
    }
  }
})
