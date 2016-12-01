import { Component, OnInit } from '@angular/core';
import {Http} from "@angular/http";
import {ADMIN_SERVICE} from "../app.component";

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {

  public statistics: {
    numClients: number;
    totalMessages:  number;
    avgMsgLength: any;
    avgMsgPerClient?: number;
  };

  constructor(
      private http: Http
  ) { }

  ngOnInit() {
    this.http.get(ADMIN_SERVICE + "/statistics").subscribe(res => {
      this.statistics = res.json();
      if (this.statistics.numClients > 0)
        this.statistics.avgMsgPerClient = this.statistics.totalMessages / this.statistics.numClients
    })
  }

}
