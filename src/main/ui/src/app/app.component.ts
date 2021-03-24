import {HttpClient} from '@angular/common/http';
import {Component, ViewChild, AfterViewInit} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import { Page } from 'ngx-pagination/dist/pagination-controls.directive';
import {merge, Observable, of as observableOf} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent  {
  chartData: any = [];
  xAxisLabel: string = '';
  yAxisLabel: string = '';
  legendTitle: string = '';
  _httpClient: HttpClient;
  filterId: number = 0;

  ngAfterViewInit() {

  }

  onSelected(filterId: number): void {
    console.log(`not yet implemented` + filterId);
    this.filterId = filterId;
  }
  byYearAndState(year: string, statePO: string): void {
    this._httpClient.get(`/president-elect/findByYearEqualsAndStatePoEquals?year=${year}&statePo=${statePO}`).subscribe(data => {
      this.chartData = data;
      this.xAxisLabel = 'Candidate`';
      this.yAxisLabel = 'Candidate Votes';
      this.legendTitle = 'By Candidate Votes Chart';
    });

  }
  findByYearBetweenAndStatePoEquals(startYear: string, endYear: string, statePO: string): void{
    this._httpClient.get(`/president-elect/findByYearBetweenAndStatePoEquals?startYear=${startYear}&endYear=${endYear}&statePo=${statePO}`).subscribe(data => {
      this.chartData = data;
      this.xAxisLabel = 'YEAR';
      this.yAxisLabel = 'Total Votes';
      this.legendTitle = 'By Total Votes Chart';
    });
}
  findByYearBetweenAndStatePo(startYear: string, endYear: string, statePO: string): void{
    this._httpClient.get(`/president-elect/findByYearBetweenAndStatePo?startYear=${startYear}&endYear=${endYear}&statePo=${statePO}`).subscribe(data => {
      this.chartData = data;
      this.xAxisLabel = 'YEAR';
      this.yAxisLabel = 'Total Votes';
      this.legendTitle = 'By Total Votes Chart';
    });
  }
  findByCandidateVotesBetweenAndYearBetween(startYear: string, endYear: string, startVotes: string, endVotes: string): void{
    this._httpClient.get(`/president-elect/findByCandidateVotesBetweenAndYearBetween?startYear=${startYear}&endYear=${endYear}&startVotes=${startVotes}&endVotes=${endVotes}`).subscribe(data => {
      this.chartData = data;
      this.xAxisLabel = 'YEAR';
      this.yAxisLabel = 'Total Votes';
      this.legendTitle = 'By Total Votes Chart';
    });
  }

  constructor(httpClient: HttpClient) {
    this._httpClient = httpClient;
  }

  getCountByMag(){
    const requestUrl = 'earthquake/countByMag';
    return this._httpClient.get(requestUrl);
  }
  getCountByLocation(){
    const requestUrl = 'earthquake/countByLocationSource';
    return this._httpClient.get(requestUrl);
  }
  getCountByTime(){
    const requestUrl = 'earthquake/countByTime';
    return this._httpClient.get(requestUrl);
  }
  onSelectChartData(filterId: number){
    if(0==filterId){
      this.getCountByMag().subscribe(data => {
        this.chartData = data;
        this.xAxisLabel = 'Magnitude';
        this.yAxisLabel = 'no of EarthQuakes';
        this.legendTitle = 'By Magnitude Chart';
      });
    }else if(1==filterId){
      this.getCountByLocation().subscribe(data => {
        this.chartData = data;
        this.xAxisLabel = 'Location Source';
        this.yAxisLabel = 'no of EarthQuakes';
        this.legendTitle = 'By Location Source Chart';
      });
    }else if(2==filterId){
      this.getCountByTime().subscribe(data => {
        this.chartData = data;
        this.xAxisLabel = 'Location Source';
        this.yAxisLabel = 'no of EarthQuakes';
        this.legendTitle = 'By Location Source Chart';
      });
    }
  }
  onSelectChartType(filterId: number){
    this.chartType = filterId;
  }
  chartType: number=0

}

export interface ChartData{
  name: string;
  value: number;
}

export interface ChartArray{
  chartArray: ChartData[];
}
