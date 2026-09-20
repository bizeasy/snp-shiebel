import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('OrderRole e2e test', () => {
  const orderRolePageUrl = '/order-role';
  let username: string;
  let password: string;
  const orderRoleSample = {};

  let orderRole;
  let party;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {
        isOrganisation: true,
        organisationName: 'abnormally',
        organisationShortName: 'potentially',
        salutation: 'yuck um gah',
        firstName: 'Everett',
        middleName: 'expert',
        lastName: 'Lockman',
        gender: 'FEMALE',
        birthDate: '2023-12-14',
        primaryPhone: 'outlying',
        primaryEmail: 'unlike',
        isTemporaryPassword: false,
        logoImageUrl: 'practical as as',
        profileImageUrl: 'below jaywalk quicker',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/order-roles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/order-roles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/order-roles/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/orders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/order-items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/role-types', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (orderRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/order-roles/${orderRole.id}`,
      }).then(() => {
        orderRole = undefined;
      });
    }
  });

  afterEach(() => {
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
  });

  it('OrderRoles menu should load OrderRoles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('order-role');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrderRole').should('exist');
    cy.location('pathname').should('eq', orderRolePageUrl);
  });

  describe('OrderRole page', () => {
    it('should have translated page title', () => {
      cy.visit(orderRolePageUrl);
      cy.getEntityHeading('OrderRole').should('not.contain', 'snpApp.orderRole.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(orderRolePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrderRole page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${orderRolePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('OrderRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderRolePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/order-roles',
          body: {
            ...orderRoleSample,
            party,
          },
        }).then(({ body }) => {
          orderRole = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/order-roles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/order-roles?page=0&size=20>; rel="last",<http://localhost/api/order-roles?page=0&size=20>; rel="first"',
              },
              body: [orderRole],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(orderRolePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OrderRole page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('orderRole');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderRolePageUrl);
      });

      it('edit button click should load edit OrderRole page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderRolePageUrl);
      });

      it('edit button click should load edit OrderRole page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrderRole');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderRolePageUrl);
      });

      it('last delete button click should delete instance of OrderRole', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('orderRole').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', orderRolePageUrl);

        orderRole = undefined;
      });
    });
  });

  describe('new OrderRole page', () => {
    beforeEach(() => {
      cy.visit(orderRolePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrderRole');
    });

    it('should create an instance of OrderRole', () => {
      cy.get(`[data-cy="party"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        orderRole = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', orderRolePageUrl);
    });
  });
});
