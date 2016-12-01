import {Component, OnInit, Input} from '@angular/core';
import {Http} from "@angular/http";
import {ADMIN_SERVICE} from "../app.component";

@Component({
    selector: 'app-chat-client-list',
    templateUrl: './chat-client-list.component.html',
    styleUrls: ['./chat-client-list.component.css']
})
export class ChatClientListComponent implements OnInit {

    @Input() chatClients: ChatClient[];

    constructor(private http: Http) {
        this.chatClients = [{
            name: "Test1"
        }, {
            name: "Test2"
        }];
    }

    ngOnInit() {
        //TODO: Get all Chat clients
        this.http.get(`${ADMIN_SERVICE}/clients`)
            .subscribe(
                res => {
                    let clients = res.json().map(c => {
                        return {name: c};
                    });
                    this.chatClients = clients;
                },
                err => console.log(err)
            )
    }

    getClientCount(client: ChatClient): void {
        this.http.get(`${ADMIN_SERVICE}/count/${client.name}`)
            .subscribe(
                count => client.count = count.json().length > 0 ? count.json()[0].messageCount : 0,
                err => console.log(err)
            );
    }

}


export interface ChatClient {
    name: string;
    count?: number;
}