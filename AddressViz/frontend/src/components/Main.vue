<template>
  <v-container fluid grid-list-md fill-height>
    <v-layout column >
        <v-flex d-flex xs1 sm1 md1>
            <!-- EditContact Dialog !-->
            <v-card color="indigo">
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
                                        v-model="contactDialogData.name"
                                    ></v-text-field>
                                    <v-text-field
                                        label="Address"
                                        v-model="contactDialogData.address"
                                    ></v-text-field>
                                    <v-text-field
                                        label="Postcode"
                                        v-model="contactDialogData.postcode"
                                    ></v-text-field>
                                    <v-text-field
                                        label="City"
                                        v-model="contactDialogData.city"
                                    ></v-text-field>
                                    <v-text-field
                                        label="Country"
                                        v-model="contactDialogData.country"
                                    ></v-text-field>
                                    <v-checkbox
                                        label="Private"
                                        v-model="contactDialogData.private"
                                    ></v-checkbox>
                                    <v-divider></v-divider>
                                    <v-text-field 
                                        label="Longitude"
                                        v-model="contactDialogData.longitude"
                                        disabled
                                    ></v-text-field>
                                    <v-text-field 
                                        label="Latitude"
                                        v-model="contactDialogData.latitude"
                                        disabled
                                    ></v-text-field>
                                    <v-divider></v-divider>
                                    <v-btn @click="updateContact(contactDialogData)">Update</v-btn>
                                    <v-btn @click="deleteContact(contactDialogData)">Delete</v-btn>
                                </v-form>
                            </v-flex>
                        </v-card-text>
                    </v-card>
                </v-dialog>
            </v-card>

            <!-- AddContact Dialog !-->
            <v-card color="indigo">
                <v-dialog v-model="addContactDialog" fullscreen hide-overlay transition="dialog-bottom-transition">
                    <v-btn slot="activator" color="orange">Add</v-btn>
                    <v-card>
                        <v-toolbar dark color="primary">
                            <v-btn icon dark @click="addContactDialog = false; addContactData = {}">
                                <v-icon>close</v-icon>
                            </v-btn>
                            <v-toolbar-title>Add Contact</v-toolbar-title>
                        </v-toolbar>
                        <v-card-text>
                            <v-flex d-flex xs6 sm6 md4>
                                <v-form>
                                    <v-text-field
                                        label="Full name"
                                        v-model="addContactData.name"
                                    ></v-text-field>
                                    <v-text-field
                                        label="Address"
                                        v-model="addContactData.address"
                                    ></v-text-field>
                                    <v-text-field
                                        label="Postcode"
                                        v-model="addContactData.postcode"
                                    ></v-text-field>
                                    <v-text-field
                                        label="City"
                                        v-model="addContactData.city"
                                    ></v-text-field>
                                    <v-text-field
                                        label="Country"
                                        v-model="addContactData.country"
                                    ></v-text-field>
                                    <v-checkbox
                                        label="Private"
                                        v-model="addContactData.private"
                                    ></v-checkbox>
                                    <v-btn @click="addContact(addContactData)">Save</v-btn>
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
                            v-for="contact in contacts"
                            :key="contact.title"
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

            //Add Coordinates to map
            this.updateLocationMarkers()
        },
        data: () => ({
            contacts: [
                { id: 1, name: "Steven B.", address: "Rosa-luxemburg-Strasse 17", postcode: "10178", city: "Berlin", country: "Germany", private: false, longitude: '', latitude: ''},
                { id: 2, name: "Lincoln M.", address: "Buchholzer Str. 10", postcode: "10437", city: "Berlin", country: "Germany", private: false, longitude: '', latitude: ''}
            ],
            map: '',
            layer_mapnik: '',
            layer_tah: '',
            layer_markers: '',
            errorDialog: false,
            errorText: '',
            editContactDialog: false,
            addContactDialog: false,
            contactDialogData: {},
            addContactData: {}
        }),
        methods: {
            addContactToMap(contact) {
                this.addMarker(
                    this.layer_markers,
                    contact.longitude,
                    contact.latitude, 
                    `<font color=\"black\"><b>${contact.name}<br>${contact.address}<br>${contact.postcode}<br>${contact.city}<br>${contact.country}</b></font>`,
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
                axios.get(`https://nominatim.openstreetmap.org/search?format=json&street=${contact.address}&city=${contact.city}&country=${contact.country}&postalcode=${contact.postalcode}`)
                .then((response) => {
                    console.log(response.data)
                    let longitude = parseFloat(response.data[0].lon)
                    let latitude = parseFloat(response.data[0].lat)
                    this.contactDialogData = JSON.parse(JSON.stringify(contact)) //Duplicate vue Data
                    this.contactDialogData.ref = contact //Save reference to original contact
                    this.editContactDialog = true
                })
                .catch((error) => {
                    console.log(error)
                    this.errorText = error
                    this.errorDialog = true
                })
            },
            showErrorDialog(error) {
                console.log(error)
                this.errorText = error
                this.errorDialog = true
            },
            updateContact(contact) {
                contact.ref.name = contact.name
                contact.ref.address = contact.address
                contact.ref.city = contact.city
                contact.ref.postcode = contact.postcode
                contact.ref.private = contact.private
                contact.ref.country = contact.country
                this.editContactDialog = false

                //Update Coordinates
                this.getContactGeoLocation(contact.ref)
                .then((location) => {
                    contact.ref.longitude = location.lon
                    contact.ref.latitude = location.lat
                    this.clearLocationMarkers()
                    this.updateLocationMarkers()
                })
                .catch((error) => {
                    this.showErrorDialog(error)
                })
            },
            updateLocationMarkers() {
                return new Promise((resolve, reject) => {
                    this.contacts.forEach((contact) => {
                        this.getContactGeoLocation(contact)
                        .then((location) => {
                            contact.longitude = location.lon
                            contact.latitude = location.lat
                            this.addContactToMap(contact)
                            resolve()
                        })
                        .catch((error) => {
                            this.showErrorDialog(error)
                        })}
                    )
                })
            },
            clearLocationMarkers() {
                this.layer_markers.clearMarkers()
            },
            deleteContact(contact) {
                this.contacts = this.contacts.filter((item) => {
                    if(item.id !== contact.id) {
                        console.log(item.name)
                        return item
                    }
                })
                this.$delete(this.contacts, contact.id)
                this.clearLocationMarkers()
                this.updateLocationMarkers().then(() => {
                    this.editContactDialog = false
                })
                
            },
            addContact(contact) {
                this.contacts.push(contact)
                this.clearLocationMarkers()
                this.updateLocationMarkers().then(() => {
                    this.addContactDialog = false
                })
                
            }
        }
    }
</script>