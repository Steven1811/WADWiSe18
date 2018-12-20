<template>
  <v-container fluid grid-list-md fill-height>
    <v-layout column >
        <v-flex d-flex xs1 sm1 md1>
            <v-card color="indigo">
                <!-- EditContact Dialog !-->
                <v-dialog v-model="editContactDialog" fullscreen hide-overlay transition="dialog-bottom-transition">
                    <v-card>
                        <v-toolbar dark color="primary">
                            <v-btn icon dark @click="editContactDialog = false">
                                <v-icon>close</v-icon>
                            </v-btn>
                            <v-toolbar-title>Edit Contact</v-toolbar-title>
                        </v-toolbar>
                        <v-card-text>
                            <v-flex d-flex xs6 sm6 md4>
                                <v-form>
                                    <v-text-field
                                        label="Full name"
                                        v-model="editContactDialogData.name"
                                    ></v-text-field>
                                    <v-text-field
                                        label="Address"
                                        v-model="editContactDialogData.address"
                                    ></v-text-field>
                                    <v-text-field
                                        label="Postcode"
                                        v-model="editContactDialogData.postcode"
                                    ></v-text-field>
                                    <v-text-field
                                        label="City"
                                        v-model="editContactDialogData.city"
                                    ></v-text-field>
                                    <v-text-field
                                        label="Country"
                                        v-model="editContactDialogData.country"
                                    ></v-text-field>
                                    <v-checkbox
                                        label="Private"
                                        v-model="editContactDialogData.private"
                                    ></v-checkbox>
                                    <v-divider></v-divider>
                                    <v-text-field 
                                        label="Longitude"
                                        v-model="editContactDialogData.longitude"
                                        disabled
                                    ></v-text-field>
                                    <v-text-field 
                                        label="Latitude"
                                        v-model="editContactDialogData.latitude"
                                        disabled
                                    ></v-text-field>
                                    <v-divider></v-divider>
                                    <v-btn @click="updateContact(editContactDialogData)">Update</v-btn>
                                    <v-btn @click="deleteContact(editContactDialogData)">Delete</v-btn>
                                </v-form>
                            </v-flex>
                        </v-card-text>
                    </v-card>
                </v-dialog>
                <!-- AddContact Dialog !-->
                <v-dialog v-model="addContactDialog" fullscreen hide-overlay transition="dialog-bottom-transition">
                    <v-btn slot="activator" color="orange">Add</v-btn>
                    <v-card>
                        <v-toolbar dark color="primary">
                            <v-btn icon dark @click="addContactDialog = false; addContactDialogData = {}">
                                <v-icon>close</v-icon>
                            </v-btn>
                            <v-toolbar-title>Add Contact</v-toolbar-title>
                        </v-toolbar>
                        <v-card-text>
                            <v-flex d-flex xs6 sm6 md4>
                                <v-form>
                                    <v-text-field
                                        label="Full name"
                                        v-model="addContactDialogData.name"
                                    ></v-text-field>
                                    <v-text-field
                                        label="Address"
                                        v-model="addContactDialogData.address"
                                    ></v-text-field>
                                    <v-text-field
                                        label="Postcode"
                                        v-model="addContactDialogData.postcode"
                                    ></v-text-field>
                                    <v-text-field
                                        label="City"
                                        v-model="addContactDialogData.city"
                                    ></v-text-field>
                                    <v-text-field
                                        label="Country"
                                        v-model="addContactDialogData.country"
                                    ></v-text-field>
                                    <v-checkbox
                                        label="Private"
                                        v-model="addContactDialogData.private"
                                    ></v-checkbox>
                                    <v-btn @click="addContact(addContactDialogData)">Save</v-btn>
                                </v-form>
                            </v-flex>
                        </v-card-text>
                    </v-card>
                </v-dialog>
            </v-card>
        </v-flex>
        <v-layout row fill-height>
            <v-flex d-flex xs3 sm3 md2>
                <v-navigation-drawer permanent clipped>
                    <v-toolbar flat color="green">
                        <v-icon>contact_mail</v-icon>
                        <v-list>
                            <v-list-tile>
                            <v-list-tile-title class="title">
                                Contacts
                            </v-list-tile-title>
                            </v-list-tile>
                        </v-list>
                    </v-toolbar>

                    <v-divider></v-divider>

                    <v-list dense class="pt-0">
                        <v-list-tile
                            v-for="(contact) in contacts"
                            :key="contact.id"
                            @click="showEditContactDialog(contact)"
                        >
                            <v-list-tile-action>
                            <v-icon>contact_mail</v-icon>
                            </v-list-tile-action>

                            <v-list-tile-content>
                            <v-list-tile-title>{{ contact.name }}</v-list-tile-title>
                            </v-list-tile-content>
                        </v-list-tile>
                    </v-list>
                </v-navigation-drawer>
            </v-flex>
            <v-layout column fill-height>
                <v-flex d-flex>
                    <div id="map" style="height=640px;width=640px;"></div>
                </v-flex>
            </v-layout>
        </v-layout>
    </v-layout>

    <v-dialog
      v-model="errorDialog"
      width="500"
    >
      <v-card>
        <v-card-title
          class="headline red"
          primary-title
        >
          Error!
        </v-card-title>

        <v-card-text>
          {{ errorText }}
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            @click="errorDialog = false"
          >OK</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </v-container>
</template>

<script>
    import axios from 'axios';
    import { mapState, mapMutations } from 'vuex'

    export default {
        
        mounted () {
            //let popuptext="<font color=\"black\"><b>Alexanderplatz Berlin</b></font>";
            //Initialize Map Viewport
            window.OpenLayers.Lang.setCode('de');

            let lon = 13.413215;
            let lat = 52.521918;
            let zoom = 15;

            this.map = new window.OpenLayers.Map('map', {
                projection: new window.OpenLayers.Projection("EPSG:900913"),
                displayProjection: new window.OpenLayers.Projection("EPSG:4326"),
                controls: [
                    new window.OpenLayers.Control.Navigation(),
                    new window.OpenLayers.Control.LayerSwitcher(),
                    new window.OpenLayers.Control.PanZoomBar()],
                maxExtent:
                    new window.OpenLayers.Bounds(-20037508.34,-20037508.34,
                                            20037508.34, 20037508.34),
                numZoomLevels: 18,
                maxResolution: 156543,
                units: 'meters'
            });

            this.layer_mapnik = new window.OpenLayers.Layer.OSM.Mapnik("Mapnik");
            this.layer_markers = new window.OpenLayers.Layer.Markers("Address", { projection: new window.OpenLayers.Projection("EPSG:4326"),
                visibility: true, displayInLayerSwitcher: false });

            this.map.addLayers([this.layer_mapnik, this.layer_markers]);
            this.jumpTo(lon, lat, zoom);


            //Add some sample data if started for the first time to prevent id inconsistencies
            if(!this.firstStart) {
                this.addNewContact({name: "Steven B.", address: "Rosa-luxemburg-Strasse 17", postcode: "10178", city: "Berlin", country: "Germany", private: false, longitude: '', latitude: ''})
                this.addNewContact({name: "Lincoln M.", address: "Buchholzer Str. 10", postcode: "10437", city: "Berlin", country: "Germany", private: false, longitude: '', latitude: ''})
                this.addNewContact({name: "Max M.", address: "Saarstraße 10", postcode: "12161", city: "Berlin", country: "Germany", private: false, longitude: '', latitude: ''})
                this.setFirstStartState(true) 
            }

            //Add Coordinates to map
            this.updateLocationMarkers()
        },
        data: () => ({
            map: '',
            layer_mapnik: '',
            layer_tah: '',
            layer_markers: '',
            errorDialog: false,
            errorText: '',
            editContactDialog: false,
            editContactDialogData: {},
            addContactDialog: false,
            addContactDialogData: {}

            
        }),
        computed: {
            ...mapState([
                'contacts',
                'firstStart'
            ])
        },
        methods: {
        ...mapMutations([
            'addNewContact',
            'deleteContactById',
            'updateContactById',
            'setFirstStartState'
        ]),

            addContactToMap(contact) {
                this.addMarker(
                    this.layer_markers,
                    contact.longitude,
                    contact.latitude, 
                    `<font color="black"><b>${contact.name}<br>${contact.address}<br>${contact.postcode}<br>${contact.city}<br>${contact.country}</b></font>`,
                    false
                )
            },
            getContactGeoLocation(contact) {
                return new Promise((resolve, reject) => {
                    axios.get(`https://nominatim.openstreetmap.org/search?format=json&street=${contact.address}&city=${contact.city}&country=${contact.country}&postalcode=${contact.postalcode}`)
                    .then((response) => {
                        let lon = parseFloat(response.data[0].lon)
                        let lat = parseFloat(response.data[0].lat)
                        resolve({ lon, lat })
                    })
                    .catch((error) => {
                        reject(error)
                    })
                })


            },
            jumpTo(lon, lat, zoom) {
                let x = this.Lon2Merc(lon);
                let y = this.Lat2Merc(lat);
                this.map.setCenter(new window.OpenLayers.LonLat(x, y), zoom);
                return false;
            },
            Lon2Merc(lon) {
                return 20037508.34 * lon / 180;
            },
            Lat2Merc(lat) {
                let PI = 3.14159265358979323846;
                lat = Math.log(Math.tan( (90 + lat) * PI / 360)) / (PI / 180);
                return 20037508.34 * lat / 180;
            },
            addMarker(layer, lon, lat, popupContentHTML, showPopup) {

                let ll = new window.OpenLayers.LonLat(this.Lon2Merc(lon), this.Lat2Merc(lat));
                let feature = new window.OpenLayers.Feature(layer, ll);
                feature.closeBox = true;
                feature.popupClass = window.OpenLayers.Class(window.OpenLayers.Popup.FramedCloud, {minSize: new window.OpenLayers.Size(300, 180) } );
                feature.data.popupContentHTML = popupContentHTML;
                feature.data.overflow = "hidden";

                let marker = new window.OpenLayers.Marker(ll);
                marker.feature = feature;

                let markerClick = function(evt) {
                    if (this.popup == null) {
                        this.popup = this.createPopup(this.closeBox);
                        this.map.addPopup(this.popup);
                        this.popup.show();
                    } else {
                        this.popup.toggle();
                    }
                    window.OpenLayers.Event.stop(evt);
                };
                marker.events.register("mousedown", feature, markerClick);

                layer.addMarker(marker);
                let popup = feature.createPopup(feature.closeBox)
                this.map.addPopup(popup);

                if(!showPopup) {
                    popup.hide();
                }
            },
            getCycleTileURL(bounds) {
                let res = this.map.getResolution();
                let x = Math.round((bounds.left - this.maxExtent.left) / (res * this.tileSize.w));
                let y = Math.round((this.maxExtent.top - bounds.top) / (res * this.tileSize.h));
                let z = this.map.getZoom();
                let limit = Math.pow(2, z);

                if (y < 0 || y >= limit)
                {
                    return null;
                }
                else
                {
                    x = ((x % limit) + limit) % limit;
                    return this.url + z + "/" + x + "/" + y + "." + this.type;
                }
            },
            showEditContactDialog(contact) {
                this.editContactDialogData = JSON.parse(JSON.stringify(contact))
                this.editContactDialog = true
            },
            showErrorDialog(error) {
                console.log(error.message)
                this.errorText = error
                this.errorDialog = true
            },
            updateContact(contact) {
                this.updateContactById({id: contact.id, data: this.editContactDialogData})
                this.updateLocationMarkers()
                .then(() => {
                    this.editContactDialog = false
                })
                .catch((error) => {
                    this.showErrorDialog(error)
                })
            },
            updateLocationMarkers() {
                return new Promise((resolve, reject) => {
                    this.layer_markers.clearMarkers()
                    this.contacts.forEach((contact) => {
                        this.getContactGeoLocation(contact)
                        .then((location) => {
                            contact.longitude = location.lon
                            contact.latitude = location.lat
                            this.addContactToMap(contact)
                            resolve()
                        })
                        .catch((error) => {
                            reject(error)
                        })}
                    )
                })
            },
            deleteContact(contact) {
                this.deleteContactById(contact.id)
                this.updateLocationMarkers().then(() => {
                    this.editContactDialog = false
                })
                
            },
            addContact(contact) {
                this.addNewContact(contact)
                this.updateLocationMarkers().then(() => {
                    this.addContactDialog = false
                    this.addContactDialogData = {}
                })
                .catch((error) => {
                    this.showErrorDialog("Address not found")
                    this.deleteContact(contact)
                })
                
            }
        }
    }
</script>