import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './need.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const NeedDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const needEntity = useAppSelector(state => state.need.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="needDetailsHeading">
          <Translate contentKey="shoppingApp.need.detail.title">Need</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{needEntity.id}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="shoppingApp.need.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{needEntity.createdAt ? <TextFormat value={needEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="shoppingApp.need.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{needEntity.quantity}</dd>
          <dt>
            <span id="deadline">
              <Translate contentKey="shoppingApp.need.deadline">Deadline</Translate>
            </span>
          </dt>
          <dd>{needEntity.deadline ? <TextFormat value={needEntity.deadline} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="shoppingApp.need.person">Person</Translate>
          </dt>
          <dd>{needEntity.person ? needEntity.person.id : ''}</dd>
          <dt>
            <Translate contentKey="shoppingApp.need.item">Item</Translate>
          </dt>
          <dd>{needEntity.item ? needEntity.item.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/need" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/need/${needEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default NeedDetail;
