/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
'use strict';

var ACTIVITI = ACTIVITI || {};

// 这里需要配置网关跳转
ACTIVITI.CONFIG = {
    'contextRoot': '',
    'authRoot': window.location.href.substring(0, window.location.href.lastIndexOf("/api") + 1) + "api/authority",
    'gateUrl': window.location.href.substring(0, window.location.href.lastIndexOf("/static") + 1)
};
ACTIVITI.CONFIG.contextRoot = ACTIVITI.CONFIG.gateUrl + 'service';
ACTIVITI.CONFIG.tenant = null;

var token = GetQueryString('token');
var tenant = GetQueryString('tenant');
localStorage.setItem('token', token);
localStorage.setItem('tenant', tenant);
ACTIVITI.CONFIG.token = "" + localStorage.getItem('token');
ACTIVITI.CONFIG.tenant = "" + localStorage.getItem('tenant');

// function GetQueryString(name) {
//     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
//     var r = window.location.search.substr(1).match(reg);
//     if(r!=null)return decodeURIComponent(r[2]); return null;
// }

function GetQueryString(n) {
    var u = self.location.href
    {
        var s = u
        if (s == null) s = self.location.href
        if (n) {
            var g = new RegExp('(\\?|&)' + n + '=([^&|#]*)')
            var r = s.match(g)
            if (r) {
                try {
                    return decodeURIComponent(r[2])
                } catch (err) {
                    return unescape(r[2])
                }
            } else return null
        } else {
            var i = s.indexOf('?')
            if (i === -1) return null
            return s.substr(i + 1)
        }
    }
}
