import {Component, OnInit} from '@angular/core';
import {Profile} from '../../shared/model/Profile';
import {ApiService} from "../../core/api.service";
import {Reward} from "../../shared/model/Model";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {

  user = new Profile();
  rewards: Reward;
  total_points;
  timeMap;
  claimMap;

  constructor(private http: ApiService) {
  }

  ngOnInit() {
    this.http.getUser().subscribe((data) => {
      this.user = data;
    });
    this.http.getUserProfile().subscribe((data) => {
      this.total_points = data.points;
      this.rewards = data.potentialRewards;
      console.log(this.rewards)
     });


  }
}
