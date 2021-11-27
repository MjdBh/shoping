import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ShoppingGroup from './shopping-group';
import ShoppingGroupDetail from './shopping-group-detail';
import ShoppingGroupUpdate from './shopping-group-update';
import ShoppingGroupDeleteDialog from './shopping-group-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShoppingGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShoppingGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShoppingGroupDetail} />
      <ErrorBoundaryRoute path={match.url} component={ShoppingGroup} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ShoppingGroupDeleteDialog} />
  </>
);

export default Routes;
