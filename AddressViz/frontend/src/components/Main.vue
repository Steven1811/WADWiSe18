<template>
  <v-container fluid grid-list-md fill-height>
    <v-layout column >
        <v-flex d-flex xs1 sm1 md1>
            <v-card color="indigo">
                <v-dialog v-model="addContactDialog" fullscreen hide-overlay transition="dialog-bottom-transition">
                    <v-btn slot="activator" color="orange">Add</v-btn>
                    <v-card>
                        <v-toolbar dark color="primary">
                            <v-btn icon dark @click="addContactDialog = false">
                                <v-icon>close</v-icon>
                            </v-btn>
                            <v-toolbar-title>Add Contact</v-toolbar-title>
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
                                    <v-btn @click="addContactDialog = false">Save</v-btn>
                                </v-form>
                            </v-flex>
                        </v-card-text>
                    </v-card>
                </v-dialog>
            </v-card>
        </v-flex>
        <v-progress-linear :indeterminate="true" v-if="!mapLoaded"></v-progress-linear>
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
                            @click="editContact(contact)"
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
            // Script Loader
            let openLayersScript = document.createElement("script")
            let openStreetMapScript = document.createElement("script")
            
            openStreetMapScript.async = false
            openLayersScript.async = false

            openStreetMapScript.setAttribute("src", "http://www.openstreetmap.org/openlayers/OpenStreetMap.js")
            openLayersScript.setAttribute("src", "http://www.openlayers.org/api/OpenLayers.js")

            document.head.appendChild(openLayersScript)
            document.head.appendChild(openStreetMapScript)

            //Sloppy but I haven't found another way to wait for the scripts to be completely loaded by the Browser
            setTimeout(() => {
                console.log("Creating Map...")
                //let popuptext="<font color=\"black\"><b>Alexanderplatz Berlin</b></font>";

                OpenLayers.Lang.setCode('de');

                let lon = 13.413215;
                let lat = 52.521918;
                let zoom = 15;

                this.map = new OpenLayers.Map('map', {
                    projection: new OpenLayers.Projection("EPSG:900913"),
                    displayProjection: new OpenLayers.Projection("EPSG:4326"),
                    controls: [
                        new OpenLayers.Control.Navigation(),
                        new OpenLayers.Control.LayerSwitcher(),
                        new OpenLayers.Control.PanZoomBar()],
                    maxExtent:
                        new OpenLayers.Bounds(-20037508.34,-20037508.34,
                                                20037508.34, 20037508.34),
                    numZoomLevels: 18,
                    maxResolution: 156543,
                    units: 'meters'
                });

                this.layer_mapnik = new OpenLayers.Layer.OSM.Mapnik("Mapnik");
                this.layer_markers = new OpenLayers.Layer.Markers("Address", { projection: new OpenLayers.Projection("EPSG:4326"),
                    visibility: true, displayInLayerSwitcher: false });

                this.map.addLayers([this.layer_mapnik, this.layer_markers]);
                this.jumpTo(lon, lat, zoom);
                this.mapLoaded = true
                //this.addMarker(this.layer_markers, 13.413215, 52.521918, popuptext);

            }, 2000);
        },
        data: () => ({
            contacts: [
                { name: "Steven B.", address: "Rosa-luxemburg-Strasse 17", postcode: "10178", city: "Berlin", country: "Germany", private: false},
                { name: "Lincoln M.", address: "Buchholzer Str. 10", postcode: "10437", city: "Berlin", country: "Germany", private: false}
            ],
            map: '',
            mapLoaded: false,
            layer_mapnik: '',
            layer_tah: '',
            layer_markers: '',
            errorDialog: false,
            errorText: '',
            addContactDialog: false,
            contactDialogData: {}
        }),
        methods: {
            jumpTo(lon, lat, zoom) {
                let x = this.Lon2Merc(lon);
                let y = this.Lat2Merc(lat);
                this.map.setCenter(new OpenLayers.LonLat(x, y), zoom);
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
            addMarker(layer, lon, lat, popupContentHTML) {

                let ll = new OpenLayers.LonLat(this.Lon2Merc(lon), this.Lat2Merc(lat));
                let feature = new OpenLayers.Feature(layer, ll);
                feature.closeBox = true;
                feature.popupClass = OpenLayers.Class(OpenLayers.Popup.FramedCloud, {minSize: new OpenLayers.Size(300, 180) } );
                feature.data.popupContentHTML = popupContentHTML;
                feature.data.overflow = "hidden";

                let marker = new OpenLayers.Marker(ll);
                marker.feature = feature;

                let markerClick = function(evt) {
                    if (this.popup == null) {
                        this.popup = this.createPopup(this.closeBox);
                        this.map.addPopup(this.popup);
                        this.popup.show();
                    } else {
                        this.popup.toggle();
                    }
                    OpenLayers.Event.stop(evt);
                };
                marker.events.register("mousedown", feature, markerClick);

                layer.addMarker(marker);
                this.map.addPopup(feature.createPopup(feature.closeBox));
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
            editContact(contact) {
                axios.get(`https://nominatim.openstreetmap.org/search?format=json&street=${contact.address}&city=${contact.city}&country=${contact.country}&postalcode=${contact.postalcode}`)
                .then((response) => {
                    console.log(response.data)
                    let longitude = parseFloat(response.data[0].lon)
                    let latitude = parseFloat(response.data[0].lat)
                    this.jumpTo(longitude, latitude, 16)
                    this.contactDialogData = contact
                    this.addContactDialog = true
                })
                .catch((error) => {
                    this.errorText = error
                })
            },
            addContact(contact) {

            }
        }
    }
</script>