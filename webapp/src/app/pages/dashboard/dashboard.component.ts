import {Component, OnChanges, OnInit} from '@angular/core';
import {ApiService} from "../../core/api.service";
import api from "@fortawesome/fontawesome";
import {Issue, Reward} from "../../shared/model/Model";
import {element} from "protractor";
import {logger} from "codelyzer/util/logger";
import {conditionallyCreateMapObjectLiteral} from "@angular/compiler/src/render3/view/util";
import {createBrowserLoggingCallback} from "@angular-devkit/build-angular/src/browser";
import {setOffsetToUTC} from "ngx-bootstrap/chronos/units/offset";

@Component({
  selector: 'openmeed-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  issues: Issue[] = [];
  repositories = [];
  admin = false;

  defaultConditions: Map<string, boolean> = new Map();
  dateConditions: Map<string, Date> = new Map();

  constructor(private http: ApiService) {
    if (sessionStorage.getItem("roles") === 'ROLE_USER') {
      this.admin = true;
    }
    this.defaultConditions.set("BENEFACTORS_MUST_BE_PR_REVIEWERS", true);
    this.defaultConditions.set("CONTRIBUTOR_MUST_BE_PR_ASSIGNEE", true);
    this.defaultConditions.set("BENEFACTORS_CANNOT_REDEEM_REWARD", true);
    this.dateConditions.set("COMPLETION_DATE", new Date(Date.now() + 860000000))
  }


  ngOnInit() {
    this.http.getRepositories().subscribe((repos) => {
      this.repositories = repos;
      console.log(this.repositories)
      this.repositories.reverse();

      this.repositories.forEach(entry => {
        this.http.getIssues(entry.fullName).subscribe((data: Issue[]) => {
          data.forEach(value => {
            this.issues.push(value);
          });

          this.http.getIssuesIncentives().subscribe(res => {
            res.forEach(entry => {
              this.updateView(entry);
            })
          });
        });
      })
    });
  }

  allocateIncentiveToIssue(event) {
    var reward = new Reward();
    reward.value = event.target.value;
    reward.issueId = event.target.dataset.url;
    reward.url = event.target.dataset.url;
    reward.htmlUrl = event.target.dataset.html_url;
    reward.claimConstraints = convertMapsToObjects(this.defaultConditions);
    reward.timeConstraints = convertMapsToObjects(this.dateConditions);

    console.log(reward);
    this.http.assignReward(reward).subscribe(result => {
        console.log(result);
        this.updateView(result);
      }
    )
  }

  updateView(entry) {
    document.getElementById(entry.issueId).innerText = entry.value.concat(entry.type);
    document.getElementById(entry.issueId.concat('-form')).style.display = "none";
    let conditionsElement = document.getElementById(entry.html_url.concat('-claim-conditions'));
    let p = document.createElement("p");
    p.style.fontWeight = '700';
    p.innerText = "REWARD CONDITIONS";
    conditionsElement.append(p);
    let claimMap = new Map<string, Date>(Object.entries(entry.claimConstraints));

    if (claimMap.size > 0) {
      claimMap.forEach((conditionValue, key) => {
        let li = document.createElement("li");
        if (conditionValue.valueOf()) {
          li.innerText = key.split('_').join(' ');
          conditionsElement.append(li);
        }
      });
    } else {
      let li = document.createElement("li");
      li.innerText = "No Claim Constraints";
      conditionsElement.append(li);
    }
    let timeMap = new Map<string, Date>(Object.entries(entry.timeConstraints));
    if (timeMap.size > 0) {
      timeMap.forEach((conditionValue, key) => {
        let li = document.createElement("li");
        if (conditionValue.valueOf()) {
          li.innerText = key.split('_').join(' ').concat(' - ').concat(new Date(conditionValue).toDateString().toLocaleUpperCase());
          conditionsElement.append(li);
        }
      });
    } else {
      let li = document.createElement("li");
      li.innerText = "No Time Constraints";
      conditionsElement.append(li);
    }
  }

}

const convertMapsToObjects = (mapInstance) => {
  const obj = {};
  for (let prop of mapInstance) {
    obj[prop[0]] = prop[1];
  }
  return obj;
};


