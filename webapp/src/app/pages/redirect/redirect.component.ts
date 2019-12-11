import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'openmeed-redirect',
  templateUrl: './redirect.component.html',
  styleUrls: ['./redirect.component.scss']
})
export class RedirectComponent implements OnInit {

  constructor(private router: Router, private activatedRoutes: ActivatedRoute) {
  }

  ngOnInit() {
    const token = this.activatedRoutes.snapshot.queryParamMap.get("token");
    const access_token = this.activatedRoutes.snapshot.queryParamMap.get("access_token");
    const roles = this.activatedRoutes.snapshot.queryParamMap.get("roles");
    const username = this.activatedRoutes.snapshot.queryParamMap.get("username");
    console.log(token,access_token,roles,username)
    if (access_token != null) {
      sessionStorage.setItem('github_access_token', access_token);
      sessionStorage.setItem('access_token', token);
      sessionStorage.setItem('roles', roles);
      sessionStorage.setItem('username', username);
      this.router.navigateByUrl('/dashboard')
    } else {
      this.router.navigateByUrl('/login')
    }
  }

}
