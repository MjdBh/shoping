import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Need from './need';
import NeedDetail from './need-detail';
import NeedUpdate from './need-update';
import NeedDeleteDialog from './need-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={NeedUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={NeedUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={NeedDetail} />
      <ErrorBoundaryRoute path={match.url} component={Need} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={NeedDeleteDialog} />
  </>
);

export default Routes;
