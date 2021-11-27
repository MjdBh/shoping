import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IShoppingGroup } from 'app/shared/model/shopping-group.model';
import { getEntities as getShoppingGroups } from 'app/entities/shopping-group/shopping-group.reducer';
import { IItem } from 'app/shared/model/item.model';
import { getEntities as getItems } from 'app/entities/item/item.reducer';
import { getEntity, updateEntity, createEntity, reset } from './person.reducer';
import { IPerson } from 'app/shared/model/person.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { PersonRole } from 'app/shared/model/enumerations/person-role.model';

export const PersonUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const shoppingGroups = useAppSelector(state => state.shoppingGroup.entities);
  const items = useAppSelector(state => state.item.entities);
  const personEntity = useAppSelector(state => state.person.entity);
  const loading = useAppSelector(state => state.person.loading);
  const updating = useAppSelector(state => state.person.updating);
  const updateSuccess = useAppSelector(state => state.person.updateSuccess);
  const personRoleValues = Object.keys(PersonRole);
  const handleClose = () => {
    props.history.push('/person' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getShoppingGroups({}));
    dispatch(getItems({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...personEntity,
      ...values,
      subscriptions: mapIdList(values.subscriptions),
      joineds: mapIdList(values.joineds),
      interests: mapIdList(values.interests),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
        }
      : {
          role: 'ADMIN',
          ...personEntity,
          createdAt: convertDateTimeFromServer(personEntity.createdAt),
          interests: personEntity?.interests?.map(e => e.id.toString()),
          subscriptions: personEntity?.subscriptions?.map(e => e.id.toString()),
          joineds: personEntity?.joineds?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="shoppingApp.person.home.createOrEditLabel" data-cy="PersonCreateUpdateHeading">
            <Translate contentKey="shoppingApp.person.home.createOrEditLabel">Create or edit a Person</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="person-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('shoppingApp.person.username')}
                id="person-username"
                name="username"
                data-cy="username"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('shoppingApp.person.name')} id="person-name" name="name" data-cy="name" type="text" />
              <ValidatedField label={translate('shoppingApp.person.role')} id="person-role" name="role" data-cy="role" type="select">
                {personRoleValues.map(personRole => (
                  <option value={personRole} key={personRole}>
                    {translate('shoppingApp.PersonRole.' + personRole)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('shoppingApp.person.createdAt')}
                id="person-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('shoppingApp.person.interests')}
                id="person-interests"
                data-cy="interests"
                type="select"
                multiple
                name="interests"
              >
                <option value="" key="0" />
                {items
                  ? items.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('shoppingApp.person.subscriptions')}
                id="person-subscriptions"
                data-cy="subscriptions"
                type="select"
                multiple
                name="subscriptions"
              >
                <option value="" key="0" />
                {shoppingGroups
                  ? shoppingGroups.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('shoppingApp.person.joined')}
                id="person-joined"
                data-cy="joined"
                type="select"
                multiple
                name="joineds"
              >
                <option value="" key="0" />
                {shoppingGroups
                  ? shoppingGroups.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/person" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PersonUpdate;
